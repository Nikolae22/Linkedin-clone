package com.backend.features.authentication.dto;

import lombok.Data;

@Data
public class AuthenticationResponseBody {

    private final String token;
    private final String message;
}
