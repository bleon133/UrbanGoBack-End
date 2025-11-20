package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "devoluciones_reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevolucionReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucion")
    private Integer idDevolucion;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_direccion_devolucion", nullable = false)
    private Direccion direccionDevolucion;

    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDate fechaDevolucion;

    @Column(name = "hora_devolucion", nullable = false)
    private LocalTime horaDevolucion;

    @Column(name = "tipo_devolucion", length = 30)
    private String tipoDevolucion;

    @Column(name = "detalles_adicionales_devolucion", columnDefinition = "TEXT")
    private String detallesAdicionalesDevolucion;
}

