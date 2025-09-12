package com.daniel.app.global.sphere.advice;



import com.daniel.app.global.sphere.exceptions.DataIntegrityCreateResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityCreateResourceException.class)
    public String handleDataIntegrityFormException(DataIntegrityCreateResourceException ex, Model model) {
        model.addAttribute("showCreateResourceForm", true);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("createResourceForm", ex.getForm());
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
