package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "permisos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tipo_usuario", "modulo"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false, length = 20)
    private TipoUsuario tipoUsuario;

    @Column(name = "modulo", nullable = false, length = 100)
    private String modulo;

    @Column(name = "puede_ver", nullable = false)
    @Builder.Default
    private boolean puedeVer = true;

    @Column(name = "puede_crear", nullable = false)
    @Builder.Default
    private boolean puedeCrear = false;

    @Column(name = "puede_editar", nullable = false)
    @Builder.Default
    private boolean puedeEditar = false;

    @Column(name = "puede_eliminar", nullable = false)
    @Builder.Default
    private boolean puedeEliminar = false;
}

