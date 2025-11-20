package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.Sucursal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    @Override
    @EntityGraph(attributePaths = {"direccion", "direccion.barrio", "direccion.barrio.ciudad"})
    Optional<Sucursal> findById(Integer integer);

    @EntityGraph(attributePaths = {"direccion", "direccion.barrio", "direccion.barrio.ciudad"})
    List<Sucursal> findAllByOrderByNombreAsc();
}
