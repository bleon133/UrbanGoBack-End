package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "barrios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barrio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barrio")
    private Integer idBarrio;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    @Column(name = "nombre_barrio", nullable = false, length = 100)
    private String nombreBarrio;
}

