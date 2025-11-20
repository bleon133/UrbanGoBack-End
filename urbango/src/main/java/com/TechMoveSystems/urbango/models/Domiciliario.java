package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "domiciliarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Domiciliario {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "disponibilidad_laboral", nullable = false)
    @Builder.Default
    private boolean disponibilidadLaboral = false;

    @Column(name = "contacto_emergencia", length = 150)
    private String contactoEmergencia;

    @Column(name = "numero_emergencia", length = 20)
    private String numeroEmergencia;

    @Column(name = "numero_licencia", nullable = false, length = 50)
    private String numeroLicencia;

    @Column(name = "categoria_moto", length = 10)
    private String categoriaMoto;

    @Column(name = "categoria_vehiculo", length = 30)
    private String categoriaVehiculo;

    @Column(name = "experiencia_previa", columnDefinition = "TEXT")
    private String experienciaPrevia;

    @Column(name = "foto_licencia_frontal", columnDefinition = "TEXT")
    private String fotoLicenciaFrontal;

    @Column(name = "foto_licencia_posterior", columnDefinition = "TEXT")
    private String fotoLicenciaPosterior;

    @Column(name = "calificacion", precision = 3, scale = 2)
    private BigDecimal calificacion;
}
