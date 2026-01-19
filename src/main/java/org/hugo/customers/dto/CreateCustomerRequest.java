package org.hugo.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be <= 120 chars")
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        @Size(max = 200, message = "email must be <= 200 chars")
        String email
) {}
