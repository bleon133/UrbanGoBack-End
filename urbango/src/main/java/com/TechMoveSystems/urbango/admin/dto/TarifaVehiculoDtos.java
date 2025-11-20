package com.TechMoveSystems.urbango.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TarifaVehiculoDtos {
    public record TarifaVehiculoSummary(
            Integer id,
            String tipoVehiculo,
            BigDecimal tarifaHora,
            BigDecimal tarifaSemana,
            BigDecimal depositoGarantia
    ) {}

    public record TarifaVehiculoDetail(
            Integer id,
            String tipoVehiculo,
            BigDecimal tarifaHora,
            BigDecimal tarifaSemana,
            BigDecimal depositoGarantia
    ) {}

    public record CreateOrUpdateTarifaVehiculoRequest(
            @NotBlank @Size(max = 30) String tipoVehiculo,
            @NotNull BigDecimal tarifaHora,
            BigDecimal tarifaSemana,
            BigDecimal depositoGarantia
    ) {}
}
