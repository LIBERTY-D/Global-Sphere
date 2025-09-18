package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.CreateComment;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import com.daniel.app.global.sphere.dtos.UpdateFeedDto;
import com.daniel.app.global.sphere.dtos.UpdateUserProfileAdmin;
import com.daniel.app.global.sphere.exceptions.FileHandlerException;
import com.daniel.app.global.sphere.models.Comment;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.Role;
import com.daniel.app.global.sphere.repository.CommentRepository;
import com.daniel.app.global.sphere.services.DashboardService;
import com.daniel.app.global.sphere.services.FeedService;
import com.daniel.app.global.sphere.services.ResourceService;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final ResourceService resourceService;
    private final FeedService feedService;
    private final CommentRepository commentRepository;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String dashBoardMain(Model model) {
        model.addAttribute("commentCount", dashboardService.commentCount());
        model.addAttribute("resourceCount", dashboardService.resourceCount());
        model.addAttribute("feedCount", dashboardService.feedCount());
        model.addAttribute("userCount", dashboardService.userCount());

        return "pages/dashboard/dashboard";
    }
    //====USERS====

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(Model model, ModelMap modelMap) {

        Boolean showModal = (Boolean) modelMap.get("showEditUserForm");
        model.addAttribute("showEditUserForm", showModal != null && showModal);
        if (!model.containsAttribute("updateUserProfileAdmin")) {
            model.addAttribute("updateUserProfileAdmin", new UpdateUserProfileAdmin());
        }
        model.addAttribute("users", userService.getAllUsers());
        return "pages/dashboard/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/dashboard/users";
    }


    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("showEditUserForm", true);
        model.addAttribute("updateUserProfileAdmin", userService.findUserById(id));
        model.addAttribute("users", userService.getAllUsers());
        return "pages/dashboard/users";
    }

    @PostMapping("/users/update")
    public String updateUser(@Valid @ModelAttribute("updateUserProfileAdmin") UpdateUserProfileAdmin dto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws FileHandlerException, IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("roles", Role.values());
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "updateUserProfileAdmin", bindingResult);
            redirectAttributes.addFlashAttribute("updateUserProfileAdmin", dto);
            redirectAttributes.addFlashAttribute("showEditUserForm", true);
            return "redirect:/dashboard/users";
        }

        var resourceImg = dto.getAvatar();
        if (resourceImg.isEmpty()) {
            redirectAttributes.addFlashAttribute("roles", Role.values());
            bindingResult.rejectValue("avatar", "avatar.invalid", "Image " + "is required");
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "updateUserProfileAdmin", bindingResult);
            redirectAttributes.addFlashAttribute("updateUserProfileAdmin", dto);
            redirectAttributes.addFlashAttribute("showEditUserForm", true);
            return "redirect:/dashboard/users";
        }

        var cntType = resourceImg.getContentType();
        if (cntType.startsWith("image/svg+xml")) {
            bindingResult.rejectValue("avatar", "avatar.invalid",
                    "SVG images not allowed");
            redirectAttributes.addFlashAttribute("roles", Role.values());
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "updateUserProfileAdmin", bindingResult);
            redirectAttributes.addFlashAttribute("updateUserProfileAdmin", dto);
            redirectAttributes.addFlashAttribute("showEditUserForm", true);
            return "redirect:/dashboard/users";
        }
        userService.updateUser(dto);

        return "redirect:/dashboard/users";
    }

    // ====RESOURCES====
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    public String getResources(Model model, ModelMap modelMap) {
        Boolean showModal = (Boolean) modelMap.get("showEditResourceForm");
        model.addAttribute("showEditResourceForm", showModal != null && showModal);
        if (!model.containsAttribute("editResourceAdminForm")) {
            model.addAttribute("editResourceAdminForm", new EditResourceDto());
        }

        model.addAttribute("resources", resourceService.getResources());
        return "pages/dashboard/resources";
    }

    @GetMapping("/resources/edit/{id}")
    public String editResource(@PathVariable Long id, Model model) {

        model.addAttribute("showEditResourceForm", true);
        model.addAttribute("editResourceAdminForm", resourceService.getResourceById(id));
        model.addAttribute("resources", resourceService.getResources());
        return "pages/dashboard/resources";
    }

    @PostMapping("/resources/update")
    public String updateResource(@Valid @ModelAttribute(
            "editResourceAdminForm") EditResourceDto dto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) throws FileHandlerException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "editResourceAdminForm", bindingResult);
            redirectAttributes.addFlashAttribute("editResourceAdminForm", dto);
            redirectAttributes.addFlashAttribute("showEditResourceForm", true);
            return "redirect:/dashboard/resources";
        }

        var resourceImg = dto.getImage();
        if (resourceImg.isEmpty()) {
            bindingResult.rejectValue("image", "editResource.image", "Image is required");
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "editResourceAdminForm", bindingResult);
            redirectAttributes.addFlashAttribute("editResourceAdminForm", dto);
            redirectAttributes.addFlashAttribute("showEditResourceForm", true);
            return "redirect:/dashboard/resources";
        }

        var cntType = resourceImg.getContentType();
        if (cntType.startsWith("image/svg+xml")) {
            bindingResult.rejectValue("image", "editResource.invalidType", "SVG images not allowed");
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "editResourceAdminForm", bindingResult);
            redirectAttributes.addFlashAttribute("editResourceAdminForm", dto);
            redirectAttributes.addFlashAttribute("showEditResourceForm", true);
            return "redirect:/dashboard/resources";
        }
        resourceService.updateResource(dto);

        return "redirect:/dashboard/resources";
    }

    @GetMapping("/resources/delete/{id}")
    public String deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return "redirect:/dashboard/resources";
    }

    //  ==== FEEDS ====

    @RequestMapping(value = "/feeds", method = RequestMethod.GET)
    public String getFeeds(Model model, ModelMap modelMap) {
        Boolean showModal = (Boolean) modelMap.get("showEditFeedModal");
        model.addAttribute("showEditFeedModal", showModal != null && showModal);
        if (!model.containsAttribute("updateFeedAdmin")) {
            model.addAttribute("updateFeedAdmin", new UpdateFeedDto());
        }
        model.addAttribute("feeds", feedService.getFeeds());
        return "pages/dashboard/feeds";
    }

    @GetMapping("/feeds/delete/{id}")
    public String deleteFeed(@PathVariable Long id) {
        feedService.deletePost(id, userService.getAuthenticatedUser());
        return "redirect:/dashboard/feeds";
    }

    @GetMapping("/feeds/edit/{id}")
    public String editFeed(@PathVariable Long id, Model model) {
        FeedItem feed = feedService.getFeedById(id);
        UpdateFeedDto updateFeedDto = new UpdateFeedDto();
        updateFeedDto.setFile(null);
        updateFeedDto.setId(id);
        updateFeedDto.setLink(feed.getLink());
        updateFeedDto.setCodeSnippet(feed.getCodeSnippet());
        updateFeedDto.setContent(feed.getContent());
        model.addAttribute("showEditFeedModal", true);
        model.addAttribute("updateFeedAdmin", updateFeedDto);
        model.addAttribute("feeds", feedService.getFeeds());
        return "pages/dashboard/feeds";
    }

    @PostMapping("/feeds/update")
    public String updateFeed(@Valid @ModelAttribute UpdateFeedDto updateFeedDto, BindingResult br, RedirectAttributes ra) throws IOException {
        if (br.hasErrors()) {
            ra.addFlashAttribute("showEditFeedModal", true);
            ra.addFlashAttribute("updateFeedAdmin", updateFeedDto);
            ra.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "updateFeedAdmin", br);
            return "redirect:/dashboard/feeds";
        }
        feedService.updateFeed(updateFeedDto);
        return "redirect:/dashboard/feeds";
    }

    // ==== COMMENTS ===
    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String getComments(ModelMap model) {
        Boolean showModal = (Boolean) model.get("showEditCommentModal");
        model.addAttribute("showEditCommentModal", showModal != null && showModal);
        if (!model.containsAttribute("createComment")) {
            model.addAttribute("createComment", new CreateComment());
        }
        model.addAttribute("comments", commentRepository.findAll());
        return "pages/dashboard/comments";
    }


    @GetMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
        return "redirect:/dashboard/comments";
    }

    @GetMapping("/comments/edit/{id}")
    public String editComment(@PathVariable Long id, Model model) {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        CreateComment createComment = CreateComment.builder()
                .postId(comment.getFeed() != null ? comment.getFeed().getId() : null)
                .author(comment.getAuthor())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt()).
                id(id)
                .build();
        model.addAttribute("showEditCommentModal", true);
        model.addAttribute("createComment", createComment);
        model.addAttribute("comments", commentRepository.findAll());
        return "pages/dashboard/comments";
    }


    @PostMapping("/comments/update")
    public String updateComment(@Valid @ModelAttribute CreateComment createComment, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("showEditCommentModal", true);
            ra.addFlashAttribute("createComment", createComment);
            ra.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "createComment", br);
            return "redirect:/dashboard/comments";
        }
        Comment comment = commentRepository.findById(createComment.getId()).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setText(createComment.getText());
        commentRepository.save(comment);
        return "redirect:/dashboard/comments";
    }

}
