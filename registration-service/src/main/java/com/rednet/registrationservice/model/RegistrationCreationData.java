package com.rednet.registrationservice.model;

import jakarta.validation.constraints.NotEmpty;

public record RegistrationCreationData(
        @NotEmpty String activationCode,
        @NotEmpty String tokenID,
        @NotEmpty String username,
        @NotEmpty String email,
        @NotEmpty String encodedPassword,
        @NotEmpty String encodedSecretWord
) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        RegistrationCreationData data = (RegistrationCreationData) obj;

        return activationCode.equals(data.activationCode) &&
                tokenID.equals(data.tokenID) &&
                username.equals(data.username) &&
                email.equals(data.email) &&
                encodedPassword.equals(data.encodedPassword) &&
                encodedSecretWord.equals(data.encodedSecretWord);
    }

    @Override
    public int hashCode() {
        return activationCode.hashCode() *
                tokenID.hashCode() *
                username.hashCode() *
                email.hashCode() *
                encodedPassword.hashCode() *
                encodedSecretWord.hashCode();
    }
}
