package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direcciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;

    @Column(name = "direccion_completa", nullable = false, length = 255)
    private String direccionCompleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barrio")
    private Barrio barrio; // nullable según la tabla

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;
}
