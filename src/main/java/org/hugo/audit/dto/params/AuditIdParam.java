package org.hugo.audit.dto.params;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.PathParam;

public class AuditIdParam {

    @PathParam("id")
    @Min(value = 1, message = "id must be >= 1")
    public long id;
}
