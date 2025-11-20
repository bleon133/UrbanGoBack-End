package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_domiciliario")
    private Usuario domiciliario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_transporte", nullable = false)
    private Transporte transporte;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarifa_vehiculo", nullable = false)
    private TarifaVehiculo tarifaVehiculo;

    @Column(name = "instrucciones_especiales", columnDefinition = "TEXT")
    private String instruccionesEspeciales;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    // Default NOW() in DB; mark insertable=false to allow DB default to be used
    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}

