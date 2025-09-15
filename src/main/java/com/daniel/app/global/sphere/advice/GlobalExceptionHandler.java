package com.daniel.app.global.sphere.advice;


import com.daniel.app.global.sphere.exceptions.DataIntegrityException;
import com.daniel.app.global.sphere.exceptions.InvalidLinkException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityException.class)
    public String handleDataIntegrityFormException(DataIntegrityException ex, Model model) {
        model.addAttribute("showCreateResourceForm", true);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("createResourceForm", ex.getCreateResourceDto());
        return "resources";

    }

    @ExceptionHandler(InvalidLinkException.class)
    public String invalidLinkException(InvalidLinkException ex, Model model) {
        if (ex.getCreateResourceDto() != null) {
            DataBinder dataBinder = new DataBinder(ex.getCreateResourceDto(), "createResourceForm");
            BindingResult bindingResult = dataBinder.getBindingResult();
            bindingResult.rejectValue("externalUrl", "invalid.link", ex.getMessage());
            model.addAttribute("showCreateResourceForm", true);
            model.addAttribute("createResourceForm", ex.getCreateResourceDto());
            model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "createResourceForm", bindingResult);

        } else {
            DataBinder dataBinder = new DataBinder(ex.getEditResourceDto(), "editResourceDtoForm");
            BindingResult bindingResult = dataBinder.getBindingResult();
            bindingResult.rejectValue("externalUrl", "invalid.link", ex.getMessage());
            model.addAttribute("editResourceDtoForm", ex.getEditResourceDto());
            model.addAttribute("showEditResourceForm", true);
            model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "editResourceDtoForm", bindingResult);
        }
        return "resources";
    }


    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("error", ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
        System.out.println(ex.getMessage());
        return "error";
    }


}
