package org.hugo.audit.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hugo.audit.domain.AuditEvent;
import org.hugo.audit.domain.AuditAction;
import org.hugo.audit.domain.AuditEntityType;

import java.time.Instant;

@Schema(name = "AuditEvent", description = "Evento de auditoría registrado por el sistema")
public record AuditEventResponse(
        @Schema(description = "ID del evento de auditoría", example = "123")
        Long id,
        @Schema(description = "Tipo de entidad afectada", example = "CUSTOMER")
        AuditEntityType entityType,
        @Schema(description = "Identificador de la entidad afectada", example = "42")
        String entityId,
        @Schema(description = "Acción realizada sobre la entidad", example = "UPDATE")
        AuditAction action,
        @Schema(description = "Identificador del actor que ejecutó la acción", example = "user-99")
        String actorId,
        @Schema(description = "Identificador del request que originó el evento", example = "req-5b0c1e")
        String requestId,
        @Schema(description = "Ruta HTTP donde ocurrió la acción", example = "/api/v1/customers/42")
        String path,
        @Schema(description = "Fecha y hora de creación del evento (UTC)", example = "2025-09-23T12:34:56Z")
        Instant createdAt,
        @Schema(description = "JSON con cambios aplicados o estado resultante", example = "{\"email\":\"new@example.com\"}")
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
