package com.TechMoveSystems.urbango.user;

import com.TechMoveSystems.urbango.models.EstadoUsuario;
import com.TechMoveSystems.urbango.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var u = usuarios.findByCorreo(email).orElseThrow(
                () -> new UsernameNotFoundException("Usuario no encontrado"));

        var auths = List.of(new SimpleGrantedAuthority("ROLE_" + u.getTipoUsuario().name()));

        boolean enabled = u.getEstado() == null || u.getEstado() == EstadoUsuario.ACTIVO;
        String stored = u.getContrasena();
        String passwordToUse = (stored != null && stored.startsWith("$2")) ? stored : passwordEncoder.encode(stored != null ? stored : "");
        return new org.springframework.security.core.userdetails.User(
                u.getCorreo(), passwordToUse, enabled,
                true, true, true, auths
        );
    }
}
