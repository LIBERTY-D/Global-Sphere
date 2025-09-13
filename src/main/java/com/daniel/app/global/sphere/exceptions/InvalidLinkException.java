package com.daniel.app.global.sphere.exceptions;

import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import lombok.Getter;

@Getter
public class InvalidLinkException extends RuntimeException {
    private final EditResourceDto editResourceDto;
    private final CreateResourceDto createResourceDto;

    public InvalidLinkException(String message, CreateResourceDto form) {
        super(message);
        this.createResourceDto = form;
        this.editResourceDto = null;
    }

    public InvalidLinkException(String message, EditResourceDto form) {
        super(message);
        this.editResourceDto = form;
        this.createResourceDto = null;
    }
}

