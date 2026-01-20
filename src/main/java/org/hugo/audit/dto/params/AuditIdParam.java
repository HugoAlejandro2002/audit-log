package org.hugo.audit.dto.params;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

public class AuditIdParam {

    @PathParam("id")
    @Min(value = 1, message = "id must be >= 1")
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "ID del evento de auditoría", example = "123")
    @Schema(minimum = "1")
    public long id;
}
