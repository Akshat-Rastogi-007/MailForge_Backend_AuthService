package com.rastogi.mailforge.AuthService.error.errors;

public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(String message) {
        super(message);
    }

}
