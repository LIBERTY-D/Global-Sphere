package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import com.daniel.app.global.sphere.models.Resource;
import com.daniel.app.global.sphere.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourceController {
    // Dummy logged-in user

    // 🔹 Dummy list of resources for now
    private final List<Resource> resources = List.of(
            Resource.builder()
                    .id(1L).authorId(1L)
                    .title("Spring MVC Best Practices")
                    .description("A deep dive into layered architecture and clean code principles.")
                    .content("Here’s the full article text... You can add multiple sections, code snippets, images, etc.")
                    .imageUrl("https://picsum.photos/seed/spring/400/200")
                    .author("Admin")
                    .publishedAt(LocalDateTime.now().minusDays(2))
                    .build(),
            Resource.builder()
                    .id(2L).
                    authorId(2L)
                    .title("Understanding Dependency Injection")
                    .description("Learn how DI works in Spring and why it matters for scalable apps.")
                    .content("Full text for Dependency Injection guide goes here...")
                    .imageUrl("https://picsum.photos/seed/di/400/200")
                    .author("Instructor Jane")
                    .externalUrl("https://spring.io/guides")
                    .publishedAt(LocalDateTime.now().minusDays(5))
                    .build()
    );


    @GetMapping
    public String listResources(Model model) {
        User currentUser = new User(
                "Alex Doe",
                "Computer Science • Spring MVC",
                "https://i.pravatar.cc/96?img=5",
                "Instructor",
                "Instructor"
        );
        currentUser.setFollowersCount(120);
        currentUser.setFollowingCount(75);
        currentUser.setPostsCount(10);
        currentUser.setEmail("alex.doe@example.com");
        currentUser.setLinkedInUrl("https://linkedin.com/in/alexdoe");
        currentUser.setGithubUrl("https://github.com/alexdoe");
        currentUser.setId(1L);
        model.addAttribute("resources", resources);
        model.addAttribute("currentUser", currentUser);
        return "resources"; // resources.html
    }

    @GetMapping("/{id}")
    public String viewResource(@PathVariable Long id, Model model) {
        User currentUser = new User(
                "Alex Doe",
                "Computer Science • Spring MVC",
                "https://i.pravatar.cc/96?img=5",
                "Instructor",
                "Instructor"
        );
        currentUser.setFollowersCount(120);
        currentUser.setFollowingCount(75);
        currentUser.setPostsCount(10);
        currentUser.setEmail("alex.doe@example.com");
        currentUser.setLinkedInUrl("https://linkedin.com/in/alexdoe");
        currentUser.setGithubUrl("https://github.com/alexdoe");
        currentUser.setId(1L);

        Resource resource = resources.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (resource == null) {
            return "error/404"; // You can customize error page
        }

        model.addAttribute("resource", resource);
        model.addAttribute("currentUser", currentUser);
        return "resource"; // resources.html (detail page)
    }

    @PostMapping("/add")
    public ModelAndView createResource(CreateResourceDto resource){
        System.out.println(resource);

        return  new ModelAndView("redirect:/resources");
    }
    @PostMapping("/edit/{id}")
    public String editResource(@PathVariable Long id, EditResourceDto dto) {

        System.out.println(dto);
        return "redirect:/resources/" + id;
    }

}
