package com.rastogi.mailforge.AuthService.error.errors;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String message) {
        super(message);
    }
}
