package com.daniel.app.global.sphere.exceptions;


import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import lombok.Getter;


@Getter
public class DataIntegrityCreateResourceException extends RuntimeException {
    private final CreateResourceDto form;

    public DataIntegrityCreateResourceException(String message, CreateResourceDto form) {
        super(message);
        this.form = form;
    }

}
