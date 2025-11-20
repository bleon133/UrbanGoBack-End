package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.TarifaVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TarifaVehiculoRepository extends JpaRepository<TarifaVehiculo, Integer> {
    @Query("select t from TarifaVehiculo t order by lower(t.tipoVehiculo)")
    List<TarifaVehiculo> findAllOrdered();
}
