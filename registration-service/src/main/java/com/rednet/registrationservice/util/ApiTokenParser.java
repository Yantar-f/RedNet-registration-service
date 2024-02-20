package com.rednet.registrationservice.util;

import com.rednet.registrationservice.model.SystemTokenClaims;

public interface ApiTokenParser {
    SystemTokenClaims parseApiToken(String token);
}
