package com.rastogi.mailforge.AuthService.error.errors;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
