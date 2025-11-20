package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ciudades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Integer idCiudad;

    @Column(name = "nombre_ciudad", nullable = false, length = 100)
    private String nombreCiudad;
}

