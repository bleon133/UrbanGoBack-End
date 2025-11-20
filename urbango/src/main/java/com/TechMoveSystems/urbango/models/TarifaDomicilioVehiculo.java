package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tarifas_domicilio_vehiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaDomicilioVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa_domicilio_vehiculo")
    private Integer idTarifaDomicilioVehiculo;

    @Column(name = "tipo_vehiculo", nullable = false, length = 30)
    private String tipoVehiculo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}

