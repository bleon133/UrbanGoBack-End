package com.TechMoveSystems.urbango.services;

import com.TechMoveSystems.urbango.models.RefreshToken;
import com.TechMoveSystems.urbango.models.Usuario;
import com.TechMoveSystems.urbango.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;

    @Value("${security.jwt.refresh-exp-days:7}")
    private int refreshExpDays;

    private static final SecureRandom RNG = new SecureRandom();

    public static String generatePlainToken() {
        byte[] bytes = new byte[48]; // 384 bits ~ 64 base64url chars
        RNG.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    public record CreatedToken(String refreshToken, RefreshToken entity) { }

    @org.springframework.transaction.annotation.Transactional
    public CreatedToken create(Usuario user, String ip, String userAgent) {
        String plain = generatePlainToken();
        String hash = sha256(plain);
        var now = Instant.now();
        var entity = RefreshToken.builder()
                .usuario(user)
                .tokenHash(hash)
                .creadoEn(now)
                .expiraEn(now.plus(refreshExpDays, ChronoUnit.DAYS))
                .revocado(false)
                .ipCreacion(ip)
                .userAgent(userAgent)
                .build();
        repo.save(entity);
        return new CreatedToken(plain, entity);
    }

    @org.springframework.transaction.annotation.Transactional
    public CreatedToken rotate(String refreshTokenPlain, String ip, String userAgent) {
        var hash = sha256(refreshTokenPlain);
        var stored = repo.findByTokenHash(hash)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));
        if (stored.isRevocado() || stored.getExpiraEn().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expirado o revocado");
        }
        // Estrategia estable: NO rotamos en cada refresh para evitar condiciones de carrera;
        // devolvemos el mismo refresh token y solo se revoca en logout o al expirar.
        return new CreatedToken(refreshTokenPlain, stored);
    }

    @org.springframework.transaction.annotation.Transactional
    public void revoke(String refreshTokenPlain) {
        var hash = sha256(refreshTokenPlain);
        repo.findByTokenHash(hash).ifPresent(rt -> {
            rt.setRevocado(true);
            rt.setRevocadoEn(Instant.now());
            repo.save(rt);
        });
    }
}
