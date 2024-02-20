package com.rednet.registrationservice.service;

import com.rednet.registrationservice.entity.Registration;
import com.rednet.registrationservice.model.RegistrationCreationData;

public interface RegistrationService {
    Registration createRegistration(RegistrationCreationData creationData);
    Registration getRegistrationByID(String ID);
    void updateRegistration(Registration updatedRegistration);
    void deleteRegistrationByID(String ID);
}
