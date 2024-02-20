package com.rednet.registrationservice.repository;

import com.rednet.registrationservice.entity.Registration;

import java.util.Optional;

public interface RegistrationRepository {
    void insert(Registration registration);
    void update(Registration registration);
    void deleteBy(String registrationID);
    boolean existsBy(String registrationID);
    Optional<Registration> findBy(String registrationID);
}
