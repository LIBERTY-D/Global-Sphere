package com.daniel.app.global.sphere.controllers;


import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import com.daniel.app.global.sphere.models.Resource;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.services.ResourceService;
import com.daniel.app.global.sphere.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final UserService userService;
    private final ResourceService resourceService;


    @GetMapping
    public String listResources(Model model) {
        User currentUser = userService.getAuthenticatedUser();
        model.addAttribute("resources", resourceService.getResources());
        model.addAttribute("currentUser", currentUser);
        return "resources"; // resources.html
    }

    @GetMapping("/{id}")
    public String viewResource(@PathVariable Long id, Model model) {
        User currentUser = userService.getAuthenticatedUser();

        Resource resource = resourceService.getResources().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (resource == null) {
            return "error/404"; // You can customize error page
        }

        model.addAttribute("resource", resource);
        model.addAttribute("currentUser", currentUser);
        return "resource";
    }

    @PostMapping("/add")
    public ModelAndView createResource(@Valid @ModelAttribute(
            "createResourceForm") CreateResourceDto resource, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("resources");
        }
        resourceService.createResource(resource);
        return new ModelAndView("redirect:/resources");
    }

    @PostMapping("/edit/{id}")
    public String editResource(@PathVariable Long id, EditResourceDto dto) {

        System.out.println(dto);
        return "redirect:/resources/" + id;
    }

}
