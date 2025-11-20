package com.TechMoveSystems.urbango.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentPreferenceRequest {
    @NotBlank(message = "El título es requerido")
    private String title;

    @NotNull(message = "El precio es requerido")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    private String description;

    @NotNull(message = "El ID del pedido es requerido")
    private Long pedidoId;
}