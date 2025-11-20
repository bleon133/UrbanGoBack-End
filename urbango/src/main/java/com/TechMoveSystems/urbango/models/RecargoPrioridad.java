package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "recargos_prioridad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecargoPrioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recargo_prioridad")
    private Integer idRecargoPrioridad;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}

