package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.EditProfileDto;
import com.daniel.app.global.sphere.dtos.ForgotPassword;
import com.daniel.app.global.sphere.dtos.SignIn;
import com.daniel.app.global.sphere.dtos.SignUp;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.services.FeedService;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FeedService feedService;



    @RequestMapping(value = {"", "/", "home"}, method = RequestMethod.GET)
    public String getHomePage(Model model) {
        // Dummy logged-in user
        User currentUser = userService.getAuthenticatedUser();
        // Sample feed
        List<FeedItem> feedItems = feedService.getFeeds();

        // Add attributes to the model
        model.addAttribute("feed", feedItems);
        model.addAttribute("currentUser", currentUser);

        return "home";
    }

    @PostMapping(value = "/register")

    public  String registerUser(@Valid @ModelAttribute("signUpForm") SignUp signUp, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("showPasswordSignUpModal",true);
            return "home";
        }

//        TODO: register user
        userService.registerUser(signUp);
        System.out.println(signUp);

        model.addAttribute("showPasswordSignUpModal",false);
        return  "home";

    }

    @PostMapping(value = "/signin")
    public String signInUser(@Valid @ModelAttribute("signinForm") SignIn signIn,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("showSignInModal", true);
            return "home";
        }

        // TODO: LOGIN USER
        userService.loginUser(signIn);
        model.addAttribute("showSignInModal", false);
        return "home";
    }


    @PostMapping(value = "/forgot-password")
    public String forgotPassword(@Valid @ModelAttribute("forgotForm") ForgotPassword forgot,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("showPasswordForgotModal", true);
            return "home";
        }

        System.out.println(forgot);
        model.addAttribute("showPasswordForgotModal", false);
        return "home";
    }

    @PostMapping("/profile/edit")
    public String editProfile(
            @Valid @ModelAttribute("editProfileForm") EditProfileDto editProfileDto,
            BindingResult bindingResult,
            Model model) {
        if (!editProfileDto.getAvatar().isEmpty()) {
            if (!editProfileDto.getAvatar().getContentType().startsWith("image/")) {
                bindingResult.rejectValue("avatar", "avatar.invalidType", "Only image files are allowed");
            }
//            if (editProfileDto.getAvatar().getSize() > 2 * 1024 * 1024) { // 2MB limit
//                bindingResult.rejectValue("avatar", "avatar.size", "File size must not exceed 2MB");
//            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showEditProfileModal", true);
            return "home";
        }

        // TODO: Save user profile update
        System.out.println(editProfileDto);

        return "redirect:/home";
    }

}
