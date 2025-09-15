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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private static final Logger log = LoggerFactory.getLogger(ResourceController.class);
    public static final String ORG_SPRINGFRAMEWORK_VALIDATION_BINDING_RESULT_EDIT_RESOURCE_DTO_FORM = "org.springframework.validation.BindingResult.editResourceDtoForm";
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
        Resource resource = resourceService.getResources().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (resource == null) return "error/404";
        if (!model.containsAttribute("editResourceDtoForm")) {
            EditResourceDto dto = new EditResourceDto();
            dto.setTitle(resource.getTitle());
            dto.setDescription(resource.getDescription());
            dto.setContent(resource.getContent());
            dto.setExternalUrl(resource.getExternalUrl());
            model.addAttribute("editResourceDtoForm", dto);
        }

        model.addAttribute("resource", resource);
        model.addAttribute("currentUser", userService.getAuthenticatedUser());
        return "resource";
    }


    @PostMapping("/add")
    public String createResource(@Valid @ModelAttribute("createResourceForm") CreateResourceDto resource, BindingResult bindingResult, Model model) {
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

    @PostMapping("/edit/{id}")
    public String editResource(
            @PathVariable Long id,
            @Valid @ModelAttribute("editResourceDtoForm") EditResourceDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws FileHandlerException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(ORG_SPRINGFRAMEWORK_VALIDATION_BINDING_RESULT_EDIT_RESOURCE_DTO_FORM, bindingResult);
            redirectAttributes.addFlashAttribute("editResourceDtoForm", dto);
            redirectAttributes.addFlashAttribute("showEditResourceForm", true);
            return "redirect:/resources/" + id;
        }

        var resourceImg = dto.getImage();
        if (resourceImg.isEmpty()) {
            bindingResult.rejectValue("image", "editResource.image", "Image is required");
            redirectAttributes.addFlashAttribute(ORG_SPRINGFRAMEWORK_VALIDATION_BINDING_RESULT_EDIT_RESOURCE_DTO_FORM, bindingResult);
            redirectAttributes.addFlashAttribute("editResourceDtoForm", dto);
            redirectAttributes.addFlashAttribute("showEditResourceForm", true);
            return "redirect:/resources/" + id;
        }

        var cntType = resourceImg.getContentType();
        if (cntType.startsWith("image/svg+xml")) {
            bindingResult.rejectValue("image", "editResource.invalidType", "SVG images not allowed");
            redirectAttributes.addFlashAttribute(ORG_SPRINGFRAMEWORK_VALIDATION_BINDING_RESULT_EDIT_RESOURCE_DTO_FORM, bindingResult);
            redirectAttributes.addFlashAttribute("editResourceDtoForm", dto);
            redirectAttributes.addFlashAttribute("showEditResourceForm", true);
            return "redirect:/resources/" + id;
        }
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
