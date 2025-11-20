package com.TechMoveSystems.urbango.repositories;

import com.TechMoveSystems.urbango.models.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Integer> {
    Optional<CuentaBancaria> findBySucursal_IdSucursal(Integer idSucursal);
    void deleteBySucursal_IdSucursal(Integer idSucursal);
}
