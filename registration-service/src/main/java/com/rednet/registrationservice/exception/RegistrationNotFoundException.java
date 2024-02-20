package com.rednet.registrationservice.exception;

public class RegistrationNotFoundException extends RuntimeException {
    public RegistrationNotFoundException(String registrationID) {
        super("Registration " + registrationID + " not found");
    }
}
