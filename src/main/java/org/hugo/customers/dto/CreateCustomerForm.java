package org.hugo.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestForm;

@Schema(name = "CreateCustomerForm", description = "Formulario multipart para crear cliente")
public class CreateCustomerForm {
    @RestForm
    @NotBlank(message = "name is required")
    @Size(max = 120, message = "name must be <= 120 chars")
    @Schema(description = "Nombre del cliente", example = "Ada Lovelace", maxLength = 120)
    public String name;

    @RestForm
    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 200, message = "email must be <= 200 chars")
    @Schema(description = "Email del cliente", example = "ada@example.com", maxLength = 200)
    public String email;
}
