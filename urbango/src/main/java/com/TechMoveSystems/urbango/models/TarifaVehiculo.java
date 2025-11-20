package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tarifas_vehiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa_vehiculo")
    private Integer idTarifaVehiculo;

    @Column(name = "tipo_vehiculo", nullable = false, length = 30)
    private String tipoVehiculo;

    @Column(name = "tarifa_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaHora;

    @Column(name = "tarifa_dia", precision = 10, scale = 2)
    private BigDecimal tarifaDia;

    @Column(name = "tarifa_semana", precision = 10, scale = 2)
    private BigDecimal tarifaSemana;

    @Column(name = "deposito_garantia", precision = 10, scale = 2)
    private BigDecimal depositoGarantia;
}

