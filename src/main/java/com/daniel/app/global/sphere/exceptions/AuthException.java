package com.daniel.app.global.sphere.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthException extends Exception {
    private final String field;
    private final String message;
}

