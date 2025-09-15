package com.daniel.app.global.sphere.exceptions;


import com.daniel.app.global.sphere.dtos.CreateFeedDto;
import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import lombok.Getter;


@Getter
public class DataIntegrityException extends RuntimeException {
    private final CreateResourceDto createResourceDto;
    private final CreateFeedDto createPostForm;

    public DataIntegrityException(String message, CreateResourceDto form) {
        super(message);
        this.createResourceDto = form;
        this.createPostForm = null;
    }

    public DataIntegrityException(String message, CreateFeedDto form) {
        super(message);
        this.createResourceDto = null;
        this.createPostForm = form;
    }

}


