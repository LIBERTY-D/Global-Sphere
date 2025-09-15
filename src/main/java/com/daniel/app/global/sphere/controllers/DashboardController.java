package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.repository.CommentRepository;
import com.daniel.app.global.sphere.services.DashboardService;
import com.daniel.app.global.sphere.services.FeedService;
import com.daniel.app.global.sphere.services.ResourceService;
import com.daniel.app.global.sphere.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "pages/dashboard/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/dashboard/users";
    }

    // ====RESOURCES====
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    public String getResources(Model model) {
        model.addAttribute("resources", resourceService.getResources());
        return "pages/dashboard/resources";
    }

    @GetMapping("/resources/delete/{id}")
    public String deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return "redirect:/dashboard/resources";
    }

    //  ==== FEEDS ====

    @RequestMapping(value = "/feeds", method = RequestMethod.GET)
    public String getFeeds(Model model) {
        model.addAttribute("feeds", feedService.getFeeds());
        return "pages/dashboard/feeds";
    }

    @GetMapping("/feeds/delete/{id}")
    public String deleteFeed(@PathVariable Long id) {
        feedService.deletePost(id, userService.getAuthenticatedUser());
        return "redirect:/dashboard/feeds";
    }

    // ==== COMMENTS ===

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String getComments(Model model) {
        model.addAttribute("comments", commentRepository.findAll());
        return "pages/dashboard/comments";
    }

    @GetMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
        return "redirect:/dashboard/comments";
    }


}
