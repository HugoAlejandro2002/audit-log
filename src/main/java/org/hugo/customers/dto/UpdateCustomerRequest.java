package org.hugo.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(

        @Size(max = 120, min = 3, message = "name must be <= 120 chars")
        String name,

        @Email(message = "email must be valid")
        @Size(max = 200, message = "email must be <= 200 chars")
        String email,

        Boolean active
) {}
