package org.hugo.audit.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.hugo.audit.dto.AuditEventResponse;
import org.hugo.audit.dto.params.AuditIdParam;
import org.hugo.audit.dto.params.AuditListParams;
import org.hugo.audit.services.AuditService;
import org.hugo.common.logging.AppLog;
import org.hugo.common.logging.AppLogger;
import org.hugo.common.response.ApiResponse;

import java.util.List;

@Path("/audit")
@Consumes("application/json")
@Produces("application/json")
@Tag(name = "Audit", description = "API to query audit events")
public class AuditResource {

    private static final AppLogger LOG = AppLog.get(AuditResource.class);

    private final AuditService service;

    public AuditResource(AuditService service) {
        this.service = service;
    }

    @GET
    @Operation(
            summary = "List audit events",
            description = "Lists events filtered by entity type/ID, actor, request, and date range. Sorted by date descending."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Audit events list",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "OK", value = """
                                            {
                                              "success": true,
                                              "data": [
                                                {
                                                  "id": 123,
                                                  "entityType": "CUSTOMER",
                                                  "entityId": "42",
                                                  "action": "UPDATE",
                                                  "actorId": "user-99",
                                                  "requestId": "req-5b0c1e",
                                                  "path": "/api/v1/customers/42",
                                                  "createdAt": "2025-09-23T12:34:56Z",
                                                  "changesJson": "{\\"email\\":\\"new@example.com\\"}"
                                                }
                                              ],
                                              "meta": {
                                                "requestId": "req-5b0c1e",
                                                "timestamp": "2025-09-23T12:34:56Z",
                                                "path": "/api/v1/audit"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid parameters or incorrect date range",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "BadRequest", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "BAD_REQUEST",
                                                "message": "Validation failed",
                                                "details": [
                                                  {"field": "page", "issue": "page must be >= 0"}
                                                ]
                                              },
                                              "meta": {
                                                "requestId": "req-5b0c1e",
                                                "timestamp": "2025-09-23T12:34:56Z",
                                                "path": "/api/v1/audit"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "InternalError", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INTERNAL_ERROR",
                                                "message": "Unexpected error"
                                              },
                                              "meta": {
                                                "requestId": "req-5b0c1e",
                                                "timestamp": "2025-09-23T12:34:56Z",
                                                "path": "/api/v1/audit"
                                              }
                                            }
                                            """)
                            }
                    )
            )
    })
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
    @Operation(
            summary = "Get audit event by ID",
            description = "Returns the audit event if it exists. If not found, the response is successful with data=null."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Event found or data=null if not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "OK", value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "id": 123,
                                                "entityType": "CUSTOMER",
                                                "entityId": "42",
                                                "action": "UPDATE",
                                                "actorId": "user-99",
                                                "requestId": "req-5b0c1e",
                                                "path": "/api/v1/customers/42",
                                                "createdAt": "2025-09-23T12:34:56Z",
                                                "changesJson": "{\\"email\\":\\"new@example.com\\"}"
                                              },
                                              "meta": {
                                                "requestId": "req-5b0c1e",
                                                "timestamp": "2025-09-23T12:34:56Z",
                                                "path": "/api/v1/audit/123"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "BadRequest", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "BAD_REQUEST",
                                                "message": "Validation failed",
                                                "details": [
                                                  {"field": "id", "issue": "id must be >= 1"}
                                                ]
                                              },
                                              "meta": {
                                                "requestId": "req-5b0c1e",
                                                "timestamp": "2025-09-23T12:34:56Z",
                                                "path": "/api/v1/audit/0"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "InternalError", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INTERNAL_ERROR",
                                                "message": "Unexpected error"
                                              },
                                              "meta": {
                                                "requestId": "req-5b0c1e",
                                                "timestamp": "2025-09-23T12:34:56Z",
                                                "path": "/api/v1/audit/123"
                                              }
                                            }
                                            """)
                            }
                    )
            )
    })
    public ApiResponse<AuditEventResponse> get(
            @Valid @BeanParam AuditIdParam p,
            @Context UriInfo uri
    ) {
        LOG.info("Fetching audit event id={}", p.id);
        return ApiResponse.ok(service.getById(p.id), uri.getPath());
    }
}
