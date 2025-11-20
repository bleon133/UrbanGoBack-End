package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    @Override
    @EntityGraph(attributePaths = {"direccion", "direccion.barrio", "direccion.barrio.ciudad"})
    Optional<Usuario> findById(Integer id);
}
