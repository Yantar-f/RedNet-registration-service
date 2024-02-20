package com.rednet.registrationservice.entity;

import com.rednet.registrationservice.model.RegistrationCreationData;
import jakarta.validation.constraints.NotEmpty;

public class Registration {
    @NotEmpty
    private String ID;

    @NotEmpty
    private String activationCode;

    @NotEmpty
    private String tokenID;

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    @NotEmpty
    private String encodedPassword;

    @NotEmpty
    private String encodedSecretWord;

    public Registration() {}

    public Registration(String ID,
                        String activationCode,
                        String tokenID,
                        String username,
                        String email,
                        String encodedPassword,
                        String encodedSecretWord) {
        this.ID = ID;
        this.activationCode = activationCode;
        this.tokenID = tokenID;
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.encodedSecretWord = encodedSecretWord;
    }

    public Registration(String ID, RegistrationCreationData creationData) {
        this.ID = ID;
        activationCode = creationData.activationCode();
        tokenID = creationData.tokenID();
        username = creationData.username();
        email = creationData.email();
        encodedPassword = creationData.encodedPassword();
        encodedSecretWord = creationData.encodedSecretWord();
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncodedSecretWord() {
        return encodedSecretWord;
    }

    public void setEncodedSecretWord(String encodedSecretWord) {
        this.encodedSecretWord = encodedSecretWord;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        Registration registration = (Registration) obj;

        return  ID.equals(registration.ID) &&
                activationCode.equals(registration.activationCode) &&
                tokenID.equals(registration.tokenID) &&
                username.equals(registration.username) &&
                email.equals(registration.email) &&
                encodedPassword.equals(registration.encodedPassword) &&
                encodedSecretWord.equals(registration.encodedSecretWord);
    }

    @Override
    public int hashCode() {
        return  ID.hashCode() *
                activationCode.hashCode() *
                tokenID.hashCode() *
                username.hashCode() *
                email.hashCode() *
                encodedPassword.hashCode() *
                encodedSecretWord.hashCode();
    }
}
