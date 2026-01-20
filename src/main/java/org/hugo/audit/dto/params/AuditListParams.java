package org.hugo.audit.dto.params;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

public class AuditListParams {

    @QueryParam("page")
    @DefaultValue("0")
    @Min(value = 0, message = "page must be >= 0")
    @Parameter(name = "page", in = ParameterIn.QUERY, description = "Page number (0-index)", example = "0")
    @Schema(minimum = "0")
    public int page;

    @QueryParam("size")
    @DefaultValue("20")
    @Min(value = 1, message = "size must be >= 1")
    @Max(value = 100, message = "size must be <= 100")
    @Parameter(name = "size", in = ParameterIn.QUERY, description = "Page size (1-100)", example = "20")
    @Schema(minimum = "1", maximum = "100")
    public int size;

    @QueryParam("entityType")
    @Size(max = 80, message = "entityType too long")
    @Parameter(name = "entityType", in = ParameterIn.QUERY, description = "Entity type filter", example = "CUSTOMER")
    public String entityType;

    @QueryParam("entityId")
    @Size(max = 64, message = "entityId too long")
    @Parameter(name = "entityId", in = ParameterIn.QUERY, description = "Entity ID filter", example = "42")
    public String entityId;

    @QueryParam("requestId")
    @Size(max = 80, message = "requestId too long")
    @Parameter(name = "requestId", in = ParameterIn.QUERY, description = "Request ID for tracing", example = "req-5b0c1e")
    public String requestId;

    @QueryParam("actorId")
    @Size(max = 80, message = "actorId too long")
    @Parameter(name = "actorId", in = ParameterIn.QUERY, description = "Actor ID who performed the action", example = "user-99")
    public String actorId;

    @QueryParam("from")
    @Size(max = 40, message = "from too long")
    @Parameter(name = "from", in = ParameterIn.QUERY, description = "Start date/time (ISO-8601 UTC)", example = "2025-01-01T00:00:00Z")
    public String from;

    @QueryParam("to")
    @Size(max = 40, message = "to too long")
    @Parameter(name = "to", in = ParameterIn.QUERY, description = "End date/time (ISO-8601 UTC)", example = "2025-01-31T23:59:59Z")
    public String to;
}
