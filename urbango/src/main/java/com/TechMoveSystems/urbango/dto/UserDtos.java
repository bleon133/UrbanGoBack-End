package com.TechMoveSystems.urbango.dto;

import com.TechMoveSystems.urbango.models.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class UserDtos {
    public record UserSummary(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String documentNumber,
            String documentType,
            String userType,
            String profilePhoto,
            boolean isActive
    ) { }

    public record UserDetail(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String documentNumber,
            TipoDocumento documentType,
            LocalDate birthDate,
            Genero gender,
            String phone,
            TipoUsuario userType,
            EstadoUsuario estado,
            String profilePhoto,
            String homeAddress,
            String addressDetail,
            Integer neighborhoodId,
            String neighborhoodName,
            Integer cityId,
            String cityName,
            String postalCode,
            // Domiciliario
            Boolean disponibilidadLaboral,
            String contactoEmergencia,
            String numeroEmergencia,
            String numeroLicencia,
            String categoriaMoto,
            String categoriaVehiculo,
            String experienciaPrevia,
            String fotoLicenciaFrontal,
            String fotoLicenciaPosterior
    ) { }

    public record CreateUserRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @Email @NotBlank String email,
            @NotBlank String password,
            @NotNull TipoDocumento documentType,
            @NotBlank String documentNumber,
            LocalDate birthDate,
            Genero gender,
            String phone,
            @NotNull TipoUsuario userType,
            // optional photo path will be set by server when file is received
            // Domiciliario
            Boolean disponibilidadLaboral,
            String contactoEmergencia,
            String numeroEmergencia,
            String numeroLicencia,
            String categoriaMoto,
            String categoriaVehiculo,
            String experienciaPrevia
    ) { }
    // Dirección opcional (para CLIENTE u otros)
    public record OptionalAddress(
            String direccionCompleta,
            Integer barrioId,
            String codigoPostal,
            String detalle
    ) { }

    public record ChangePasswordRequest(@NotBlank String newPassword) { }
}
