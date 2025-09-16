package com.daniel.app.global.sphere.controllers;

import com.daniel.app.global.sphere.dtos.Person;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.Resource;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.services.FeedService;
import com.daniel.app.global.sphere.services.ResourceService;
import com.daniel.app.global.sphere.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final UserService userService;
    private final FeedService feedService;
    private final ResourceService resourceService;


    @RequestMapping(value = {"", "/", "home"}, method = RequestMethod.GET)
    public String getHomePage(@RequestParam(name = "q", required = false) String query, Model model) {
        User currentUser = userService.getAuthenticatedUser();

        List<FeedItem> feeds;
        if (query != null && !query.isBlank()) {
            feeds = feedService.searchFeeds(query);
        } else {
            feeds = feedService.getFeeds();
        }

        List<Resource> featuredResource = resourceService.getFeaturedResources();
        List<Person> peoples = userService.peopleToFollow();

        model.addAttribute("feeds", feeds);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("follow", peoples);
        model.addAttribute("featuredResources", featuredResource);
        model.addAttribute("searchQuery", query);

        return "pages/home/home";
    }

}
