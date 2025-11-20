package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_transporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_transporte")
    private Integer idTipoTransporte;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
}
