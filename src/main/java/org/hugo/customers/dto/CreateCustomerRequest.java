package org.hugo.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CreateCustomerRequest", description = "Payload para crear un cliente")
public record CreateCustomerRequest(
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be <= 120 chars")
        @Schema(description = "Nombre del cliente", example = "Ada Lovelace", maxLength = 120)
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        @Size(max = 200, message = "email must be <= 200 chars")
        @Schema(description = "Email del cliente", example = "ada@example.com", maxLength = 200)
        String email
) {}
