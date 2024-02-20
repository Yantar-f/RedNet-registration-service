package com.rednet.registrationservice.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String tokenType) {
        super("invalid token: " + tokenType);
    }
}
