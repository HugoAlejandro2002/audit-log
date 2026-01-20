package org.hugo.customers.dto.params;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

public class CustomerIdParam {
    @PathParam("id")
    @Min(value = 1, message = "id must be >= 1")
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Customer ID", example = "42")
    @Schema(minimum = "1")
    public long id;
}
