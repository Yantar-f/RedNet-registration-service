package com.rednet.registrationservice.repository.impl;

import com.rednet.registrationservice.entity.Registration;
import com.rednet.registrationservice.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RegistrationRepositoryImpl implements RegistrationRepository {
    private final RedisTemplate<String, Registration> template;
    private final long RegistrationExpirationMs;

    public RegistrationRepositoryImpl(
            RedisTemplate<String, Registration> template,
            @Value("${rednet.app.registration-expiration-ms}") long RegistrationExpirationMs
    ) {
        this.template = template;
        this.RegistrationExpirationMs = RegistrationExpirationMs;
    }

    public void insert(Registration registration) {
        template.opsForValue().set(
                registration.getID(),
                registration,
                RegistrationExpirationMs,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void update(Registration registration) {
        template.opsForValue().set(registration.getID(), registration);
    }

    public Optional<Registration> findBy(String registrationID) {
        return Optional.ofNullable(template.opsForValue().get(registrationID));
    }

    public void deleteBy(String registrationID) {
        template.opsForValue().getOperations().delete(registrationID);
    }

    @Override
    public boolean existsBy(String registrationID) {
        return template.opsForValue().get(registrationID) != null;
    }
}
