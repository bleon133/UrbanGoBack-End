package com.TechMoveSystems.urbango.auth;

import com.TechMoveSystems.urbango.auth.dto.AuthDtos.*;
import com.TechMoveSystems.urbango.models.Usuario;
import com.TechMoveSystems.urbango.repositories.UsuarioRepository;
import com.TechMoveSystems.urbango.security.JwtService;
import com.TechMoveSystems.urbango.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarios;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokens;

    @Transactional
    public void register(RegisterRequest req) {
        // Mantener implementación original si se usaba app_user, o migrar aquí a usuarios
        throw new UnsupportedOperationException("Endpoint de registro pendiente de migración a 'usuarios'");
    }

    public AuthResponse login(LoginRequest req, HttpServletRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        Jwt jwt = jwtService.encode(auth);
        long expiresIn = jwt.getExpiresAt().getEpochSecond() - jwt.getIssuedAt().getEpochSecond();
        var principal = auth.getName();
        var role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        Usuario usuario = usuarios.findByCorreo(principal)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado tras autenticación"));
        var created = refreshTokens.create(usuario, request.getRemoteAddr(), request.getHeader("User-Agent"));

        return new AuthResponse(jwt.getTokenValue(), expiresIn, "Bearer", principal, role, created.refreshToken());
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest req, HttpServletRequest request) {
        var rotated = refreshTokens.rotate(req.refreshToken(), request.getRemoteAddr(), request.getHeader("User-Agent"));
        var user = rotated.entity().getUsuario();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getCorreo(), "N/A",
                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getTipoUsuario().name()))
        );
        Jwt jwt = jwtService.encode(auth);
        long expiresIn = jwt.getExpiresAt().getEpochSecond() - jwt.getIssuedAt().getEpochSecond();
        var role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return new AuthResponse(jwt.getTokenValue(), expiresIn, "Bearer", user.getCorreo(), role, rotated.refreshToken());
    }

    public void logout(RefreshRequest req) {
        refreshTokens.revoke(req.refreshToken());
    }
}
