package com.example.locationtrackingbackend.Exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
