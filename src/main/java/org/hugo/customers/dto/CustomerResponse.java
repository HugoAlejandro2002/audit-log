package org.hugo.customers.dto;

import java.time.Instant;

import org.hugo.customers.domain.Customer;

public record CustomerResponse(
        long id,
        String name,
        String email,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
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
