package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.HorarioAtencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Integer> {
    List<HorarioAtencion> findBySucursal_IdSucursal(Integer idSucursal);
    void deleteBySucursal_IdSucursal(Integer idSucursal);
}
