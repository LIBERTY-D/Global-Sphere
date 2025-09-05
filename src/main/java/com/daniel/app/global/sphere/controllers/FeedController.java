package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.CreateComment;
import com.daniel.app.global.sphere.dtos.CreateDiscussion;
import com.daniel.app.global.sphere.dtos.CreateFeedDto;
import com.daniel.app.global.sphere.dtos.UpdateFeedDto;
import com.daniel.app.global.sphere.exceptions.FileHandlerException;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.services.FeedService;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final UserService userService;

    @PostMapping("/create-feed")
    public String createFeed(@Valid @ModelAttribute("createPostForm") CreateFeedDto createFeedDto,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("showCreatePostModal", true);
            return "home";
        }
        try {
            feedService.createPost(createFeedDto);
        } catch (FileHandlerException fileHandlerException) {
            System.out.println(fileHandlerException.getMessage());
        }

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

        feedService.createComment(createComment);
        model.addAttribute("showCommentModal", false);
        return "redirect:/home";
    }

    @GetMapping("/feeds/edit/{id}")
    public String editFeed(@PathVariable Long id, Model model) {
        FeedItem post = feedService.getPostById(id);
        User currentUser = userService.getAuthenticatedUser();
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own posts.");
        }

        UpdateFeedDto updateFeedDto = new UpdateFeedDto(
                post.getContent(),
                post.getCodeSnippet(),
                post.getLink()
        );
        model.addAttribute("post", post);
        model.addAttribute("updateFeedDto", updateFeedDto);

        return "edit-feed";
    }

    @PostMapping("/feeds/like/{id}")
    @ResponseBody
    public Map<String, Object> toggleLikeFeed(@PathVariable Long id) {
        FeedItem updatedFeed = feedService.toggleLikeFeed(id);
        Map<String, Object> response = new HashMap<>();
        response.put("likes", updatedFeed.getLikes());
        return response;
    }

    @PostMapping("/feeds/edit/{id}")
    public String updateFeed(
            @PathVariable Long id,
            @Valid @ModelAttribute("updateFeedDto") UpdateFeedDto updateFeedDto,
            BindingResult bindingResult,
            Model model) {

        User currentUser = userService.getAuthenticatedUser();
        FeedItem post = feedService.getPostById(id);

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own posts.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "edit-feed";
        }

        feedService.updateFeed(
                id,
                currentUser,
                updateFeedDto.getContent(),
                updateFeedDto.getCodeSnippet(),
                updateFeedDto.getLink()
        );
        return "redirect:/home";
    }


    @PostMapping("/feeds/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        User currentUser = userService.getAuthenticatedUser();
        feedService.deletePost(id, currentUser);
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

        feedService.createDiscussion(createDiscussion);
        model.addAttribute("showDiscussionModal", false);
        return new ModelAndView("redirect:/home");
    }
}
