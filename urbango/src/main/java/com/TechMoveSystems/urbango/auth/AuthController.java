package com.TechMoveSystems.urbango.auth;

import com.TechMoveSystems.urbango.auth.dto.AuthDtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import com.TechMoveSystems.urbango.repositories.PermisoRepository;
import com.TechMoveSystems.urbango.models.TipoUsuario;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final PermisoRepository permisos;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        service.register(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req, HttpServletRequest request) {
        return ResponseEntity.ok(service.login(req, request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest req, HttpServletRequest request) {
        return ResponseEntity.ok(service.refresh(req, request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest req) {
        service.logout(req);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal Jwt jwt) {
        return new Object() {
            public final String email = jwt.getSubject();
            public final String scope = jwt.getClaim("scope");
            public final String tokenId = jwt.getId(); // puede venir null si no seteas jti
        };
    }

    @GetMapping("/permissions")
    public List<Map<String, Object>> myPermissions(@AuthenticationPrincipal Jwt jwt) {
        String scope = jwt.getClaim("scope"); // e.g. "ROLE_ADMINISTRADOR"
        String role = (scope != null && scope.contains("ROLE_")) ? scope.replace("ROLE_", "") : (scope != null ? scope : "CLIENTE");
        TipoUsuario tipo;
        try { tipo = TipoUsuario.valueOf(role); } catch (Exception e) { tipo = TipoUsuario.CLIENTE; }
        return permisos.findByTipoUsuario(tipo).stream()
                .map(p -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("modulo", p.getModulo());
                    m.put("puedeVer", p.isPuedeVer());
                    m.put("puedeCrear", p.isPuedeCrear());
                    m.put("puedeEditar", p.isPuedeEditar());
                    m.put("puedeEliminar", p.isPuedeEliminar());
                    return m;
                })
                .toList();
    }
}
