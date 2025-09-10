package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.EditProfileDto;
import com.daniel.app.global.sphere.dtos.ForgotPassword;
import com.daniel.app.global.sphere.dtos.SignIn;
import com.daniel.app.global.sphere.dtos.SignUp;
import com.daniel.app.global.sphere.exceptions.AuthException;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/register")
    public String registerUser(@Valid @ModelAttribute("signUpForm") SignUp signUp, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("showPasswordSignUpModal", true);
            return "home";
        }
        try {
            userService.registerUser(signUp);
        } catch (DataIntegrityViolationException exp) {
            bindingResult.rejectValue("email", "error.signinForm", "email " + "taken");
            model.addAttribute("showPasswordSignUpModal", true);
            return "home";

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
            return "home";
        }
        return "redirect:/home";
    }

    @PostMapping(value = "/forgot-password")
    public String forgotPassword(@Valid @ModelAttribute("forgotForm") ForgotPassword forgot, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("showPasswordForgotModal", true);
            return "home";
        }
        model.addAttribute("showPasswordForgotModal", false);
        return "home";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@Valid @ModelAttribute("editProfileForm") EditProfileDto editProfileDto, BindingResult bindingResult, Model model) throws IOException {
        if (!editProfileDto.getAvatar().isEmpty()) {
            String cntType = editProfileDto.getAvatar().getContentType();
            if (!cntType.startsWith("image/")) {
                bindingResult.rejectValue("avatar", "avatar.invalidType", "Only image files are allowed");
                model.addAttribute("showEditProfileModal", true);
                return "home";
            }
            if (cntType.startsWith("image/svg+xml")) {
                bindingResult.rejectValue("avatar", "avatar.invalidType",
                        "Image type svg+xml not allowed" +
                                " ");
                model.addAttribute("showEditProfileModal", true);
                return "home";
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showEditProfileModal", true);
            return "home";
        }

        Boolean res = userService.updateUser(editProfileDto);
        return "redirect:/home";
    }

    @GetMapping("/user/image")
    @ResponseBody
    public byte[] getAuthenticatedUserImage() {
        return userService.getUserImage();
    }

    @GetMapping(value = "/user/{id}/avatar", produces =
            MediaType.IMAGE_JPEG_VALUE)
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

}
