package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "recogidas_reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecogidaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recogida")
    private Integer idRecogida;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_direccion_recogida", nullable = false)
    private Direccion direccionRecogida;

    @Column(name = "fecha_recogida", nullable = false)
    private LocalDate fechaRecogida;

    @Column(name = "hora_recogida", nullable = false)
    private LocalTime horaRecogida;

    @Column(name = "tipo_recogida", length = 30)
    private String tipoRecogida;

    @Column(name = "detalles_adicionales_recogida", columnDefinition = "TEXT")
    private String detallesAdicionalesRecogida;
}

