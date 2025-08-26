package com.daniel.app.global.sphere.controllers;

import com.daniel.app.global.sphere.dtos.CreateComment;
import com.daniel.app.global.sphere.dtos.CreateDiscussion;
import com.daniel.app.global.sphere.dtos.CreatePost;
import com.daniel.app.global.sphere.services.FeedService;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final UserService userService;
    private final FeedService feedService;

    @PostMapping("/create-post")
    public String createPost(@Valid @ModelAttribute("createPostForm") CreatePost createPost,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("showCreatePostModal", true);
            return "home";
        }

        // TODO: Save the post
        System.out.println(createPost);

        return "redirect:/home";
    }


    @PostMapping("/comments/add")
    public String createComment(@Valid @ModelAttribute("createCommentForm") CreateComment createComment,
                                      BindingResult bindingResult,
                                      Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("showCommentModal", true);
            return "home";
        }

        // TODO: save comment to DB
        System.out.println(createComment);
        model.addAttribute("showCommentModal", false);
        return "redirect:/home";
    }


    @PostMapping("/discussions/add")
    public ModelAndView createDiscussion(
            @Valid @ModelAttribute("createDiscussionForm") CreateDiscussion createDiscussion,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("showDiscussionModal", true);
            return new ModelAndView("home");
        }

        // TODO: save discussion to DB
        System.out.println(createDiscussion);
        model.addAttribute("showDiscussionModal", false);
        return new ModelAndView("redirect:/home");
    }

}
