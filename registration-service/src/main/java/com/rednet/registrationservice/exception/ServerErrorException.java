package com.rednet.registrationservice.exception;

public class ServerErrorException extends RuntimeException {
    private ServerErrorException(String message) {
        super(message);
    }
}
