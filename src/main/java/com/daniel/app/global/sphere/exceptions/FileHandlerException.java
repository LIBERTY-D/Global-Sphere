package com.daniel.app.global.sphere.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
public class FileHandlerException extends RuntimeException {
    private final String field;
    private final String message;
}
