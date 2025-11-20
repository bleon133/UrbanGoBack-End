package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tarifas_domicilios_normales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaDomicilioNormal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa_domicilio")
    private Integer idTarifaDomicilio;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad_entrega")
    private Ciudad ciudadEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barrio_entrega")
    private Barrio barrioEntrega;
}

