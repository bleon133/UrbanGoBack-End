package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sucursales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "razon_social", length = 150)
    private String razonSocial;

    @Column(name = "nit", length = 30)
    private String nit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @Column(name = "latitud", precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 9, scale = 6)
    private BigDecimal longitud;

    @Column(name = "persona_contacto", length = 100)
    private String personaContacto;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "correo_contacto", length = 150)
    private String correoContacto;

    @Column(name = "representante_legal", length = 150)
    private String representanteLegal;

    @Column(name = "documento_representante", length = 30)
    private String documentoRepresentante;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto;
}

