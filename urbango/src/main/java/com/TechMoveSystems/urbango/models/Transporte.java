package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transporte")
    private Integer idTransporte;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_transporte", nullable = false)
    private TipoTransporte tipoTransporte;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "placa", length = 20, unique = true)
    private String placa;

    @Column(name = "peso", precision = 10, scale = 2)
    private BigDecimal peso;

    @Column(name = "velocidad_max", precision = 5, scale = 2)
    private BigDecimal velocidadMax;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
}

