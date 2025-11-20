package com.TechMoveSystems.urbango.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder jwtEncoder;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.exp-minutes}")
    private long expMinutes;

    public Jwt encode(Authentication auth) {
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)    // p.ej. ROLE_USER
                .collect(Collectors.joining(" "));      // "ROLE_USER ROLE_ADMIN"

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expMinutes * 60))
                .subject(auth.getName())               // email/username
                .claim("scope", scope)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims));
    }
}
