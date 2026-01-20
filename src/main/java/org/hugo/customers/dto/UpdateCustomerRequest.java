package org.hugo.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UpdateCustomerRequest", description = "Payload para actualizar un cliente (campos opcionales)")
public record UpdateCustomerRequest(

        @Size(max = 120, min = 3, message = "name must be <= 120 chars")
        @Schema(description = "Nuevo nombre", example = "Ada Byron", minLength = 3, maxLength = 120)
        String name,

        @Email(message = "email must be valid")
        @Size(max = 200, message = "email must be <= 200 chars")
        @Schema(description = "Nuevo email", example = "ada.byron@example.com", maxLength = 200)
        String email,

        @Schema(description = "Estado de actividad del cliente", example = "true")
        Boolean active
) {}
