package org.hugo.audit.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "audit_events",
        indexes = {
                @Index(name = "idx_audit_entity", columnList = "entity_type, entity_id, created_at"),
                @Index(name = "idx_audit_actor", columnList = "actor_id, created_at"),
                @Index(name = "idx_audit_request", columnList = "request_id")
        }
)
public class AuditEvent extends PanacheEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 50)
    public AuditEntityType entityType;

    @Column(name = "entity_id", nullable = false, length = 64)
    public String entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 30)
    public AuditAction action;

    @Column(name = "actor_id", length = 80)
    public String actorId;

    @Column(name = "request_id", length = 80)
    public String requestId;

    @Column(name = "path", length = 200)
    public String path;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    // JSON con cambios por campo (recomendado)
    @Lob
    @Column(name = "changes_json", columnDefinition = "LONGTEXT")
    public String changesJson;

    // Snapshots opcionales (útiles si quieres)
    @Lob
    @Column(name = "before_json", columnDefinition = "LONGTEXT")
    public String beforeJson;

    @Lob
    @Column(name = "after_json", columnDefinition = "LONGTEXT")
    public String afterJson;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
    }
}
