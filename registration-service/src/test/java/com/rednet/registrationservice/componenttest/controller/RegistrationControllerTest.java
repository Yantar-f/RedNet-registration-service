package com.rednet.registrationservice.componenttest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednet.registrationservice.controller.RegistrationController;
import com.rednet.registrationservice.entity.Registration;
import com.rednet.registrationservice.exception.RegistrationNotFoundException;
import com.rednet.registrationservice.exception.ServerErrorException;
import com.rednet.registrationservice.exception.handler.GlobalExceptionHandler;
import com.rednet.registrationservice.model.RegistrationCreationData;
import com.rednet.registrationservice.service.RegistrationService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.instancio.Instancio.create;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(print = MockMvcPrint.SYSTEM_OUT, addFilters = false)
@Import({RegistrationController.class, GlobalExceptionHandler.class})
public class RegistrationControllerTest {
    @MockBean
    RegistrationService registrationService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void Creating_registration_is_successful() throws Exception {
        RegistrationCreationData creationData = create(RegistrationCreationData.class);
        Registration expectedRegistration = create(Registration.class);

        when(registrationService.createRegistration(eq(creationData)))
                .thenReturn(expectedRegistration);

        String responseBody = mvc.perform(post("/registrations")
                        .content(objectMapper.writeValueAsString(creationData))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Registration actualRegistration = objectMapper.readValue(responseBody, Registration.class);

        assertEquals(expectedRegistration, actualRegistration);
    }

    @Test
    public void Creating_registration_with_invalid_content_type_is_not_successful() throws Exception {
        RegistrationCreationData creationData = create(RegistrationCreationData.class);

        mvc.perform(post("/registrations")
                        .content(objectMapper.writeValueAsString(creationData)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));

        verify(registrationService, never())
                .createRegistration(eq(creationData));
    }

    @ParameterizedTest
    @MethodSource("registrationCreationDataStringFieldsNames")
    public void Creating_registration_with_blank_field_is_not_successful(String fieldName) throws Exception {
        RegistrationCreationData creationData = Instancio.of(RegistrationCreationData.class)
                .set(field(fieldName), "")
                .create();

        mvc.perform(post("/registrations")
                        .content(objectMapper.writeValueAsString(creationData))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }

    @ParameterizedTest
    @MethodSource("registrationCreationDataFieldsNames")
    public void Creating_registration_with_nullable_field_is_not_successful(String fieldName) throws Exception {
        RegistrationCreationData creationData = Instancio.of(RegistrationCreationData.class)
                .ignore(field(fieldName))
                .create();

        mvc.perform(post("/registrations")
                        .content(objectMapper.writeValueAsString(creationData))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }

    private static List<String> registrationCreationDataFieldsNames() {
        return getClassFieldsNames(RegistrationCreationData.class);
    }

    private static List<String> registrationCreationDataStringFieldsNames() {
        return getClassStringFieldsNames(RegistrationCreationData.class);
    }

    @Test
    public void Getting_registration_by_id_is_successful() throws Exception {
        Registration expectedRegistration = create(Registration.class);

        when(registrationService.getRegistrationByID(eq(expectedRegistration.getID())))
                .thenReturn(expectedRegistration);

        String responseBody = mvc.perform(get("/registrations/by-id")
                        .param("id", expectedRegistration.getID()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Registration actualRegitration = objectMapper.readValue(responseBody, Registration.class);

        assertEquals(expectedRegistration, actualRegitration);
    }

    @Test
    public void Getting_not_existing_registration_by_id_is_not_successful() throws Exception {
        String ID = create(String.class);

        when(registrationService.getRegistrationByID(eq(ID)))
                .thenThrow(RegistrationNotFoundException.class);

        mvc.perform(get("/registrations/by-id")
                        .param("id", ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }

    @Test
    public void Getting_registration_withoud_id_param_is_not_successful() throws Exception {
        mvc.perform(get("/registrations/by-id"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }

    @Test
    public void Updating_registration_is_successful() throws Exception {
        Registration updatingRegistration = create(Registration.class);

        mvc.perform(put("/registrations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatingRegistration)))
                .andExpect(status().isNoContent());

        verify(registrationService).updateRegistration(eq(updatingRegistration));
    }

    @Test
    public void Updating_registration_with_invalid_content_type_is_not_successful() throws Exception {
        Registration updatingRegistration = create(Registration.class);

        mvc.perform(put("/registrations")
                        .content(objectMapper.writeValueAsString(updatingRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));

        verify(registrationService, never())
                .updateRegistration(eq(updatingRegistration));
    }

    @Test
    public void Updating_not_existing_registration_is_not_successful() throws Exception {
        Registration updatingRegistration = create(Registration.class);

        doThrow(RegistrationNotFoundException.class)
                .when(registrationService).updateRegistration(eq(updatingRegistration));

        mvc.perform(put("/registrations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatingRegistration)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));

        verify(registrationService).updateRegistration(eq(updatingRegistration));
    }

    @ParameterizedTest
    @MethodSource("registrationStringFieldsNames")
    public void Updating_registration_with_blank_string_field_is_not_successful(String fieldName) throws Exception {
        Registration updatingRegistration = Instancio.of(Registration.class)
                .set(field(fieldName), "")
                .create();

        mvc.perform(put("/registrations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatingRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));

        verify(registrationService, never())
                .updateRegistration(eq(updatingRegistration));
    }

    @ParameterizedTest
    @MethodSource("registrationFieldsNames")
    public void Updating_registration_with_nullable_field_is_not_successful(String fieldName) throws Exception {
        Registration updatingRegistration = Instancio.of(Registration.class)
                .ignore(field(fieldName))
                .create();

        mvc.perform(put("/registrations")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatingRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));

        verify(registrationService, never())
                .updateRegistration(eq(updatingRegistration));
    }

    private static List<String> registrationFieldsNames() {
        return getClassFieldsNames(Registration.class);
    }

    private static List<String> registrationStringFieldsNames() {
        return getClassStringFieldsNames(Registration.class);
    }

    @Test
    public void Deleting_registration_by_id_is_successful() throws Exception {
        String id = create(String.class);

        mvc.perform(delete("/registrations/by-id")
                        .param("id", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void Deleting_registration_without_id_param_is_not_successful() throws Exception {
        mvc.perform(delete("/registrations/by-id"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }

    @Test
    public void Deleting_not_existing_registration_by_id_is_not_successful() throws Exception {
        String id = create(String.class);

        doThrow(RegistrationNotFoundException.class)
                .when(registrationService).deleteRegistrationByID(eq(id));

        mvc.perform(delete("/registrations/by-id")
                        .param("id", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));

    }

    private static List<String> getClassFieldsNames(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                .map(Field::getName)
                .toList();
    }

    private static List<String> getClassStringFieldsNames(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                .filter(c -> c.getType().equals(String.class))
                .map(Field::getName)
                .toList();
    }
}
