package org.hugo.audit.dto;

import org.hugo.audit.domain.AuditEvent;
import org.hugo.audit.domain.AuditAction;
import org.hugo.audit.domain.AuditEntityType;

import java.time.Instant;

public record AuditEventResponse(
        Long id,
        AuditEntityType entityType,
        String entityId,
        AuditAction action,
        String actorId,
        String requestId,
        String path,
        Instant createdAt,
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
