package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "recargos_tamanos_paquete")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecargoTamanoPaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recargo_tamano")
    private Integer idRecargoTamano;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}

