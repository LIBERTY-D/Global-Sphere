package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.CreateComment;
import com.daniel.app.global.sphere.dtos.CreateDiscussion;
import com.daniel.app.global.sphere.dtos.CreateFeedDto;
import com.daniel.app.global.sphere.dtos.UpdateFeedDto;
import com.daniel.app.global.sphere.exceptions.FileHandlerException;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.ImageEntity;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.ImageRepository;
import com.daniel.app.global.sphere.services.FeedService;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final UserService userService;
    private final ImageRepository imageRepository;

    @PostMapping("/create-feed")
    public String createFeed(@Valid @ModelAttribute("createFeedForm") CreateFeedDto createFeedDto, BindingResult bindingResult, Model model) {

        if (!createFeedDto.getFile().isEmpty()) {
            String cntType = createFeedDto.getFile().getContentType();
            if (!cntType.startsWith("image/")) {
                bindingResult.rejectValue("file", "avatar.invalidType", "Only" +
                        " image files are allowed");
                model.addAttribute("showCreatePostModal", true);
                return "pages/home/home";
            }
            if (cntType.startsWith("image/svg+xml")) {
                bindingResult.rejectValue("file", "avatar.invalidType", "Image type svg+xml not allowed");
                model.addAttribute("showCreatePostModal", true);
                return "pages/home/home";
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showCreatePostModal", true);
            return "pages/home/home";
        }
        try {
            feedService.createFeed(createFeedDto);
        } catch (FileHandlerException fileHandlerException) {
            bindingResult.rejectValue("file", "something wrong with your " +
                    "image");
            model.addAttribute("showCreatePostModal", true);
            return "pages/home/home";
        }

        return "redirect:/home";
    }

    @PostMapping("/comments/add")
    public String createComment(@Valid @ModelAttribute("createCommentForm") CreateComment createComment, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "createCommentForm", bindingResult);
            redirectAttributes.addFlashAttribute("createCommentForm", createComment);
            redirectAttributes.addFlashAttribute("showCommentModal", true);
            return "redirect:/home";
        }
        feedService.createComment(createComment);
        model.addAttribute("showCommentModal", false);
        return "redirect:/home";
    }

    @GetMapping("/feeds/edit/{id}")
    public String editFeed(@PathVariable Long id, Model model) {
        FeedItem feed = feedService.getFeedById(id);

        UpdateFeedDto updateFeedDto = new UpdateFeedDto();
        updateFeedDto.setFile(null);
        updateFeedDto.setLink(feed.getLink());
        updateFeedDto.setCodeSnippet(feed.getCodeSnippet());
        updateFeedDto.setContent(feed.getContent());

        model.addAttribute("post", feed);
        model.addAttribute("updateFeedDto", updateFeedDto);

        return "pages/feed/edit-feed";
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
    public String updateFeed(@PathVariable Long id, @Valid @ModelAttribute("updateFeedDto") UpdateFeedDto updateFeedDto, BindingResult bindingResult, Model model) {
        User currentUser = userService.getAuthenticatedUser();
        FeedItem feed = feedService.getFeedById(id);
        if (!updateFeedDto.getFile().isEmpty()) {
            String cntType = updateFeedDto.getFile().getContentType();
            if (!cntType.startsWith("image/")) {
                bindingResult.rejectValue("file", "avatar.invalidType", "Only" + " image files are allowed");
                model.addAttribute("post", feed);
                return "pages/feed/edit-feed";
            }
            if (cntType.startsWith("image/svg+xml")) {
                model.addAttribute("post", feed);
                bindingResult.rejectValue("file", "avatar.invalidType", "Image type svg+xml not allowed");
                return "pages/feed/edit-feed";
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", feed);
            return "pages/feed/edit-feed";
        }
        try {
            model.addAttribute("post", feed);
            feedService.updateFeed(id, updateFeedDto);
        } catch (IOException exp) {
            model.addAttribute("post", feed);
            bindingResult.rejectValue("file", "something wrong with your image");
            return "pages/feed/edit-feed";
        }
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
            return new ModelAndView("pages/home/home");
        }

        feedService.createDiscussion(createDiscussion);
        model.addAttribute("showDiscussionModal", false);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/feeds/image/{id}")
    @ResponseBody
    public byte[] getFeedImage(@PathVariable Long id) {
        ImageEntity image = imageRepository.findById(id).orElseThrow();
        return image.getData();
    }

}
