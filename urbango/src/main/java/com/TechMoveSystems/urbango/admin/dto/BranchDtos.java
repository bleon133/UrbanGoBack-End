package com.TechMoveSystems.urbango.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class BranchDtos {

    public record BranchSummary(
            Integer id,
            String nombre,
            String razonSocial,
            String nit,
            String personaContacto,
            String telefonoContacto,
            String correoContacto,
            boolean activa
    ) {}

    public record BranchDetail(
            Integer id,
            String nombre,
            String razonSocial,
            String nit,
            String representanteLegal,
            String documentoRepresentante,
            String personaContacto,
            String telefonoContacto,
            String correoContacto,
            String direccion,
            Integer barrioId,
            Integer ciudadId,
            String barrioNombre,
            String ciudadNombre,
            String codigoPostal,
            BigDecimal latitud,
            BigDecimal longitud,
            String foto,
            String detalleDireccion,
            String horarioApertura,
            String horarioCierre,
            List<String> diasAtencion
    ) {}

    public record CreateOrUpdateBranchRequest(
            @NotBlank String nombre,
            @Size(max = 150) String razonSocial,
            @Size(max = 30) String nit,
            @Size(max = 150) String representanteLegal,
            @Size(max = 30) String documentoRepresentante,
            @Size(max = 100) String personaContacto,
            @Size(max = 20) String telefonoContacto,
            @Email String correoContacto,
            @NotBlank String direccion,
            Integer barrioId,
            @Size(max = 20) String codigoPostal,
            BigDecimal latitud,
            BigDecimal longitud,
            String detalleDireccion,
            String horarioApertura,
            String horarioCierre,
            List<String> diasAtencion
    ) {}
}
