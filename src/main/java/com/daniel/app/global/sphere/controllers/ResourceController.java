package com.daniel.app.global.sphere.controllers;

import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import com.daniel.app.global.sphere.exceptions.FileHandlerException;
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
        return "resources";
    }

    @GetMapping("/{id}")
    public String viewResource(@PathVariable Long id, Model model) {
        User currentUser = userService.getAuthenticatedUser();

        Resource resource = resourceService.getResources().stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);

        if (resource == null) {
            return "error/404";
        }

        showEditForm(id, model);
        model.addAttribute("resource", resource);
        model.addAttribute("currentUser", currentUser);
        return "resource";
    }

    @PostMapping("/add")
    public String createResource(@Valid @ModelAttribute("createResourceForm") CreateResourceDto resource, BindingResult bindingResult, Model model) throws FileHandlerException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("showCreateResourceForm", true);
            return "resources";
        }
        var resourceImg = resource.getImageUrl();
        if (resourceImg.isEmpty()) {
            bindingResult.rejectValue("imageUrl", "createResource.imageUrl", "image is required");

            model.addAttribute("showCreateResourceForm", true);
            return "resources";
        }
        var cntType = resource.getImageUrl().getContentType();
        if (cntType.startsWith("image/svg+xml")) {
            bindingResult.rejectValue("imageUrl", "createResource.invalidType", "Image type svg+xml not allowed" + " ");
            model.addAttribute("showCreateResourceForm", true);
            return "resources";
        }

        model.addAttribute("showCreateResourceForm", false);
        resourceService.createResource(resource);
        return "redirect:/resources";
    }


    private void showEditForm(@PathVariable Long id, Model model) {
        var resource = resourceService.getResourceById(id);
        model.addAttribute("editResourceDtoForm", resource);
        model.addAttribute("showEditResourceForm", true);

    }

    @PostMapping("/edit/{id}")
    public String editResource(@PathVariable Long id,
                               @ModelAttribute("editResourceDtoForm") EditResourceDto dto,
                               BindingResult bindingResult,
                               Model model) throws FileHandlerException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("showEditResourceForm", true);
           return   "redirect:/resources/" + id;
        }

        var resourceImg = dto.getImage();
        if (resourceImg.isEmpty()) {
            bindingResult.rejectValue("image", "editResource.image", "image is required");
            model.addAttribute("showEditResourceForm", true);
            return "redirect:/resources/" + id;
        }

        var cntType = dto.getImage().getContentType();
        if (cntType.startsWith("image/svg+xml")) {
            bindingResult.rejectValue("image", "editResource.invalidType", "SVG images not allowed");
            model.addAttribute("showEditResourceForm", true);
            return "redirect:/resources/" + id;
        }
        model.addAttribute("showEditResourceForm", false);
        resourceService.updateResource(id, dto);
        return "redirect:/resources/" + id;
    }


    @PostMapping("/delete/{id}")
    public String deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return "redirect:/resources";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public byte[] getFeedImage(@PathVariable Long id) {
        return resourceService.resourceImage(id);
    }


}
