package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @EntityGraph(attributePaths = {"usuario"})
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    long deleteByUsuario_IdUsuario(Integer idUsuario);
}
