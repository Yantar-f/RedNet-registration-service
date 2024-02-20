package com.rednet.registrationservice.service.impl;

import com.rednet.registrationservice.entity.Registration;
import com.rednet.registrationservice.exception.RegistrationNotFoundException;
import com.rednet.registrationservice.model.RegistrationCreationData;
import com.rednet.registrationservice.repository.RegistrationRepository;
import com.rednet.registrationservice.service.RegistrationService;
import com.rednet.registrationservice.util.RegistrationIDGenerator;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RegistrationIDGenerator registrationIDGenerator;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                   RegistrationIDGenerator registrationIDGenerator) {
        this.registrationRepository = registrationRepository;
        this.registrationIDGenerator = registrationIDGenerator;
    }

    @Override
    public Registration createRegistration(RegistrationCreationData creationData) {
        String registrationID = registrationIDGenerator.generate();
        Registration registration = new Registration(registrationID, creationData);

        registrationRepository.insert(registration);

        return registration;
    }

    @Override
    public Registration getRegistrationByID(String ID) {
        return registrationRepository
                .findBy(ID)
                .orElseThrow(() -> new RegistrationNotFoundException(ID));
    }

    @Override
    public void updateRegistration(Registration registration) {
        checkRegistrationExistence(registration);
        registrationRepository.update(registration);
    }

    @Override
    public void deleteRegistrationByID(String ID) {
        registrationRepository.deleteBy(ID);
    }

    private void checkRegistrationExistence(Registration registration) {
        if (! registrationRepository.existsBy(registration.getID()))
            throw new RegistrationNotFoundException(registration.getID());
    }
}
