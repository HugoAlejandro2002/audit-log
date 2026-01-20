package org.hugo.audit.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hugo.audit.domain.AuditEvent;
import org.hugo.audit.domain.AuditAction;
import org.hugo.audit.domain.AuditEntityType;

import java.time.Instant;

@Schema(name = "AuditEvent", description = "Audit event recorded by the system")
public record AuditEventResponse(
        @Schema(description = "Audit event ID", example = "123")
        Long id,
        @Schema(description = "Affected entity type", example = "CUSTOMER")
        AuditEntityType entityType,
        @Schema(description = "Affected entity identifier", example = "42")
        String entityId,
        @Schema(description = "Action performed on the entity", example = "UPDATE")
        AuditAction action,
        @Schema(description = "Actor identifier who performed the action", example = "user-99")
        String actorId,
        @Schema(description = "Request identifier that originated the event", example = "req-5b0c1e")
        String requestId,
        @Schema(description = "HTTP path where the action occurred", example = "/api/v1/customers/42")
        String path,
        @Schema(description = "Event creation timestamp (UTC)", example = "2025-09-23T12:34:56Z")
        Instant createdAt,
        @Schema(description = "JSON with applied changes or resulting state", example = "{\"email\":\"new@example.com\"}")
        String changesJson
) {
    public static AuditEventResponse from(AuditEvent e) {
        return new AuditEventResponse(
                e.id,
                e.entityType,
                e.entityId,
                e.action,
                e.actorId,
                e.requestId,
                e.path,
                e.createdAt,
                e.changesJson
        );
    }
}
