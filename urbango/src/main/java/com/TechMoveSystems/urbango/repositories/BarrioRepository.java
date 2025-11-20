package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.Barrio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarrioRepository extends JpaRepository<Barrio, Integer> {
    List<Barrio> findByCiudad_IdCiudadOrderByNombreBarrioAsc(Integer idCiudad);
}

