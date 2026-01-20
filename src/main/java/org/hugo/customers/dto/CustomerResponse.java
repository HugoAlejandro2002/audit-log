package org.hugo.customers.dto;

import java.time.Instant;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hugo.customers.domain.Customer;

@Schema(name = "Customer", description = "Representación pública del cliente")
public record CustomerResponse(
        @Schema(description = "ID del cliente", example = "42")
        long id,
        @Schema(description = "Nombre del cliente", example = "Ada Lovelace")
        String name,
        @Schema(description = "Email del cliente", example = "ada@example.com")
        String email,
        @Schema(description = "Indica si el cliente está activo", example = "true")
        boolean active,
        @Schema(description = "Fecha de creación (UTC)", example = "2025-01-01T10:00:00Z")
        Instant createdAt,
        @Schema(description = "Fecha de última actualización (UTC)", example = "2025-01-05T11:00:00Z")
        Instant updatedAt,
        @Schema(description = "Versión para control optimista", example = "3")
        long version
) {
    public static CustomerResponse from(Customer c) {
        return new CustomerResponse(
                c.id,
                c.name,
                c.email,
                c.active,
                c.createdAt,
                c.updatedAt,
                c.version
        );
    }
}
