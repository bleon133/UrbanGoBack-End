package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.TipoTransporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoTransporteRepository extends JpaRepository<TipoTransporte, Integer> {
    Optional<TipoTransporte> findByNombreIgnoreCase(String nombre);
}
