package org.hugo.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestForm;

@Schema(name = "UpdateCustomerForm", description = "Multipart form to update customer")
public class UpdateCustomerForm {
    @RestForm
    @Size(max = 120, min = 3, message = "name must be <= 120 chars")
    @Schema(description = "New name", example = "Ada Byron", minLength = 3, maxLength = 120)
    public String name;

    @RestForm
    @Email(message = "email must be valid")
    @Size(max = 200, message = "email must be <= 200 chars")
    @Schema(description = "New email", example = "ada.byron@example.com", maxLength = 200)
    public String email;

    @RestForm
    @Schema(description = "Customer active status", example = "true")
    public Boolean active;
}
