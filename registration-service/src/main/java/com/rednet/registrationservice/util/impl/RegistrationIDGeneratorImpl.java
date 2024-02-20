package com.rednet.registrationservice.util.impl;

import com.rednet.registrationservice.util.RegistrationIDGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationIDGeneratorImpl implements RegistrationIDGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
