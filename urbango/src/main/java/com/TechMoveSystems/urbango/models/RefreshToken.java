package com.TechMoveSystems.urbango.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue
    @Column(name = "id_refresh_token")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "expira_en", nullable = false)
    private Instant expiraEn;

    @Column(name = "revocado", nullable = false)
    private boolean revocado;

    @Column(name = "revocado_en")
    private Instant revocadoEn;

    @Column(name = "ip_creacion", length = 45)
    private String ipCreacion;

    @Column(name = "user_agent")
    private String userAgent;
}

