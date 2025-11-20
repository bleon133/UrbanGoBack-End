package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "domicilios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_domicilio")
    private Integer idDomicilio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_domiciliario", nullable = false)
    private Usuario domiciliario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_direccion_recogida", nullable = false)
    private Direccion direccionRecogida;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_direccion_entrega", nullable = false)
    private Direccion direccionEntrega;

    @Column(name = "descripcion_paquete", columnDefinition = "TEXT")
    private String descripcionPaquete;

    @Column(name = "nombre_destinatario", length = 150)
    private String nombreDestinatario;

    @Column(name = "telefono_destinatario", length = 20)
    private String telefonoDestinatario;

    @Column(name = "foto_recogida", columnDefinition = "TEXT")
    private String fotoRecogida;

    @Column(name = "foto_entrega", columnDefinition = "TEXT")
    private String fotoEntrega;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "instrucciones_especiales", columnDefinition = "TEXT")
    private String instruccionesEspeciales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarifa_domicilio_normal")
    private TarifaDomicilioNormal tarifaDomicilioNormal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recargo_prioridad")
    private RecargoPrioridad recargoPrioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recargo_tamano_paquete")
    private RecargoTamanoPaquete recargoTamanoPaquete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recargo_vehiculo_tipo")
    private RecargoDomicilioVehiculoTipo recargoVehiculoTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarifa_domicilio_vehiculo")
    private TarifaDomicilioVehiculo tarifaDomicilioVehiculo;

    // Use DB default NOW()
    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}

