package org.hugo.audit.dto.params;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class AuditListParams {

    @QueryParam("page")
    @DefaultValue("0")
    @Min(value = 0, message = "page must be >= 0")
    public int page;

    @QueryParam("size")
    @DefaultValue("20")
    @Min(value = 1, message = "size must be >= 1")
    @Max(value = 100, message = "size must be <= 100")
    public int size;

    @QueryParam("entityType")
    @Size(max = 80, message = "entityType too long")
    public String entityType;

    @QueryParam("entityId")
    @Size(max = 64, message = "entityId too long")
    public String entityId;

    @QueryParam("requestId")
    @Size(max = 80, message = "requestId too long")
    public String requestId;

    @QueryParam("actorId")
    @Size(max = 80, message = "actorId too long")
    public String actorId;

    @QueryParam("from")
    @Size(max = 40, message = "from too long")
    public String from;

    @QueryParam("to")
    @Size(max = 40, message = "to too long")
    public String to;
}
