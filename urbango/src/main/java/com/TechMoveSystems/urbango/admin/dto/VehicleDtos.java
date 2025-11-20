package com.TechMoveSystems.urbango.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VehicleDtos {

    public record VehicleSummary(
            Integer id,
            String tipoVehiculo,
            String marca,
            String modelo,
            Integer anio,
            String placa,
            String color,
            String estado,
            Integer sucursalId,
            String sucursalNombre,
            String foto
    ) {}

    public record VehicleDetail(
            Integer id,
            Integer sucursalId,
            String sucursalNombre,
            String tipoVehiculo,
            String marca,
            String modelo,
            Integer anio,
            String placa,
            BigDecimal peso,
            BigDecimal velocidadMax,
            String color,
            String estado,
            String foto,
            LocalDate vencimientoTecnomecanica,
            LocalDate vencimientoSoat,
            LocalDate fechaMantenimientoPreventivo
    ) {}

    public record CreateVehicleRequest(
            @NotNull Integer sucursalId,
            @NotBlank @Size(max = 30) String tipoVehiculo,
            @Size(max = 50) String marca,
            @Size(max = 50) String modelo,
            Integer anio,
            @Size(max = 20) String placa,
            BigDecimal peso,
            BigDecimal velocidadMax,
            @Size(max = 30) String color,
            @NotBlank @Size(max = 20) String estado,
            LocalDate vencimientoTecnomecanica,
            LocalDate vencimientoSoat,
            LocalDate fechaMantenimientoPreventivo
    ) {}
}
