package org.hugo.customers.dto;

import java.time.Instant;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hugo.customers.domain.Customer;

@Schema(name = "Customer", description = "Public representation of the customer")
public record CustomerResponse(
        @Schema(description = "Customer ID", example = "42")
        long id,
        @Schema(description = "Customer name", example = "Ada Lovelace")
        String name,
        @Schema(description = "Customer email", example = "ada@example.com")
        String email,
        @Schema(description = "Indicates whether the customer is active", example = "true")
        boolean active,
        @Schema(description = "Creation date (UTC)", example = "2025-01-01T10:00:00Z")
        Instant createdAt,
        @Schema(description = "Last update date (UTC)", example = "2025-01-05T11:00:00Z")
        Instant updatedAt,
        @Schema(description = "Version for optimistic control", example = "3")
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
