package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.Transporte;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransporteRepository extends JpaRepository<Transporte, Integer> {

    @Override
    @EntityGraph(attributePaths = {"sucursal", "tipoTransporte"})
    Optional<Transporte> findById(Integer id);

    @Override
    @EntityGraph(attributePaths = {"sucursal", "tipoTransporte"})
    List<Transporte> findAll();
}
