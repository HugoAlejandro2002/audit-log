package org.hugo.audit.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

import org.hugo.audit.dto.AuditEventResponse;
import org.hugo.audit.dto.params.AuditIdParam;
import org.hugo.audit.dto.params.AuditListParams;
import org.hugo.audit.services.AuditService;
import org.hugo.common.logging.AppLog;
import org.hugo.common.logging.AppLogger;
import org.hugo.common.response.ApiResponse;

import java.util.List;

@Path("/api/audit")
@Consumes("application/json")
@Produces("application/json")
public class AuditResource {

    private static final AppLogger LOG = AppLog.get(AuditResource.class);

    private final AuditService service;

    public AuditResource(AuditService service) {
        this.service = service;
    }

    @GET
    public ApiResponse<List<AuditEventResponse>> list(
            @Valid @BeanParam AuditListParams p,
            @Context UriInfo uriInfo
    ) {
        LOG.info("AuditResource.list page={} size={} entityType={} entityId={} requestIdPresent={} actorIdPresent={} from={} to={}",
                p.page, p.size, p.entityType, p.entityId,
                p.requestId != null && !p.requestId.isBlank(),
                p.actorId != null && !p.actorId.isBlank(),
                p.from, p.to);

        return ApiResponse.ok(service.list(p), uriInfo.getPath());
    }

    @GET
    @Path("/{id}")
    public ApiResponse<AuditEventResponse> get(
            @Valid @BeanParam AuditIdParam p,
            @Context UriInfo uri
    ) {
        LOG.info("Fetching audit event id={}", p.id);
        return ApiResponse.ok(service.getById(p.id), uri.getPath());
    }
}
