package com.TechMoveSystems.urbango.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    public record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank String password,
            String fullName
    ) { }

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) { }

    public record AuthResponse(
            String accessToken,
            long expiresInSeconds,
            String tokenType,
            String email,
            String role,
            String refreshToken
    ) { }

    public record RefreshRequest(
            @NotBlank String refreshToken
    ) { }
}
