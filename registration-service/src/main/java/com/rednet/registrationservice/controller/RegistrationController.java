package com.rednet.registrationservice.controller;

import com.rednet.registrationservice.entity.Registration;
import com.rednet.registrationservice.model.RegistrationCreationData;
import com.rednet.registrationservice.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Registration> createRegistration(@Valid @RequestBody RegistrationCreationData data) {
        return ResponseEntity.ok(registrationService.createRegistration(data));
    }

    @GetMapping(path = "/by-id", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Registration> getRegistrationByID(@RequestParam("id") String ID) {
        return ResponseEntity.ok(registrationService.getRegistrationByID(ID));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> updateRegistration(@Valid @RequestBody Registration registration) {
        registrationService.updateRegistration(registration);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    ResponseEntity<Void> deleteRegistrationByID(@RequestParam("id") String ID) {
        registrationService.deleteRegistrationByID(ID);
        return ResponseEntity.noContent().build();
    }
}
