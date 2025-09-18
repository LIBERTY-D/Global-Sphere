package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.*;
import com.daniel.app.global.sphere.exceptions.AuthException;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/register")
    public String registerUser(@Valid @ModelAttribute("signUpForm") SignUp signUp, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("showPasswordSignUpModal", true);
            return "pages/home/home";
        }
        try {
            userService.registerUser(signUp);
        } catch (DataIntegrityViolationException exp) {
            bindingResult.rejectValue("email", "error.signinForm", "email " + "taken");
            model.addAttribute("showPasswordSignUpModal", true);
            return "pages/home/home";

        }

        model.addAttribute("showPasswordSignUpModal", false);
        return "redirect:/home";

    }


    @PostMapping("/signin")
    public String signInUser(@Valid @ModelAttribute("signinForm") SignIn signIn, BindingResult bindingResult, Model model, HttpServletRequest request) {

        try {
            User user = userService.authenticate(signIn);

            Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            // 1. Set authentication on the context
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            // 2. Store context in the session (so Spring Security can retrieve it later)
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            model.addAttribute("showSignInModal", false);

        } catch (AuthException e) {
            bindingResult.rejectValue(e.getField(), "error.signinForm", e.getMessage());
            model.addAttribute("showSignInModal", true);
            return "pages/home/home";
        }
        return "redirect:/home";
    }

    @RequestMapping(value = "/global-sphere/reset-password", method = RequestMethod.GET)
    public String getForgotPasswordPage(@RequestParam(name = "token", required = false) String token, Model model, HttpServletRequest request) {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setToken(token);
        model.addAttribute("resetPasswordForm", dto);
        model.addAttribute("invalidToken", null);
        return "pages/forgot-password/forgot-password";
    }

    @PostMapping("/global-sphere/reset-password")
    public String handleResetPassword(@Valid @ModelAttribute("resetPasswordForm") ResetPasswordDto form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "pages/forgot-password/forgot-password";
        }
        boolean isValidUser = userService.resetPassword(form.getToken(), form.getPassword());
        if (!isValidUser) {
            model.addAttribute("invalidToken", "The reset link is invalid or expired.");
            return "pages/forgot-password/forgot-password";
        }
        redirectAttributes.addFlashAttribute("success", "Password updated successfully. Please sign in.");
        return "redirect:/home";
    }

    @PostMapping(value = "/forgot-password")
    public String forgotPassword(@Valid @ModelAttribute("forgotForm") ForgotPassword forgot, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("showPasswordForgotModal", true);
            return "pages/home/home";
        }
        userService.sendForgotPasswordEmail(forgot.getEmail());
        model.addAttribute("showPasswordForgotModal", false);
        return "pages/home/home";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@Valid @ModelAttribute("editProfileForm") EditProfileDto editProfileDto, BindingResult bindingResult, Model model) throws IOException {
        if (!editProfileDto.getAvatar().isEmpty()) {
            String cntType = editProfileDto.getAvatar().getContentType();
            if (!cntType.startsWith("image/")) {
                bindingResult.rejectValue("avatar", "avatar.invalidType", "Only image files are allowed");
                model.addAttribute("showEditProfileModal", true);
                return "pages/home/home";
            }
            if (cntType.startsWith("image/svg+xml")) {
                bindingResult.rejectValue("avatar", "avatar.invalidType", "Image type svg+xml not allowed" + " ");
                model.addAttribute("showEditProfileModal", true);
                return "pages/home/home";
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showEditProfileModal", true);
            return "pages/home/home";
        }
        userService.updateUser(editProfileDto);
        return "redirect:/home";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(Principal p, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUserByEmail(p.getName());
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/home?logout=true";
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordDto form, BindingResult result, RedirectAttributes ra, Principal principal) {

        if (result.hasErrors()) {
            ra.addFlashAttribute("showUpdatePasswordModal", true);
            ra.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "updatePasswordForm", result);
            ra.addFlashAttribute("updatePasswordForm", form);
            return "redirect:/home";
        }

        if (!userService.passwordsMatch(principal.getName(), form.getCurrentPassword())) {
            result.rejectValue("currentPassword", "currentPassword" + ".invalidMatch", "current password does match the one in " + "our system");
            ra.addFlashAttribute("showUpdatePasswordModal", true);
            ra.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "updatePasswordForm", result);
            ra.addFlashAttribute("updatePasswordForm", form);
            return "redirect:/home";

        }
        userService.updatePassword(principal.getName(), form);
        ra.addFlashAttribute("successMessage", "Password updated successfully!");
        return "redirect:/home";
    }

    @GetMapping("/user/image")
    @ResponseBody
    public byte[] getAuthenticatedUserImage() {
        return userService.getUserImage();
    }

    @GetMapping(value = "/user/{id}/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getAvatar(@PathVariable Long id) {
        User user = userService.findUserImageById(id);
        return user.getAvatar();
    }


    @GetMapping("/follow")
    public String toggleFollow(@RequestParam(value = "id", required = false) Long userId) {
        userService.toggleFollow(userId);
        return "redirect:/home";
    }

    @GetMapping("/user/follow")
    public String follow(@RequestParam(value = "id", required = false) Long userId) {
        userService.followUser(userId);
        return "redirect:/home";
    }

    @GetMapping("/user/unfollow")
    public String unfollow(@RequestParam(value = "id", required = false) Long userId) {
        userService.unfollowUser(userId);
        return "redirect:/home";
    }

    @GetMapping("/user/remove/follower")
    public String removeFollower(@RequestParam(value = "id", required = false) Long userId) {
        userService.removeFollower(userId);
        return "redirect:/home";
    }

}
