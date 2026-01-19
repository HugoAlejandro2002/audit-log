package org.hugo.audit.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.hugo.audit.domain.AuditEntityType;
import org.hugo.audit.domain.AuditEvent;

import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class AuditEventRepository implements PanacheRepository<AuditEvent> {

    public List<AuditEvent> findByEntity(
            AuditEntityType type,
            String entityId,
            int page,
            int size
    ) {
        return find("entityType = ?1 and entityId = ?2 order by createdAt desc", type, entityId)
                .page(page, size)
                .list();
    }

    public List<AuditEvent> listAllPaged(int page, int size) {
        return findAll()
                .page(page, size)
                .list();
    }

    public List<AuditEvent> listFiltered(
            int page, int size,
            String entityType,
            String entityId,
            String requestId,
            String actorId,
            Instant from,
            Instant to
    ) {
        StringBuilder q = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (entityType != null) {
            q.append(" and entityType = :entityType");
            params.put("entityType", entityType);
        }
        if (entityId != null) {
            q.append(" and entityId = :entityId");
            params.put("entityId", entityId);
        }
        if (requestId != null) {
            q.append(" and requestId = :requestId");
            params.put("requestId", requestId);
        }
        if (actorId != null) {
            q.append(" and actorId = :actorId");
            params.put("actorId", actorId);
        }
        if (from != null) {
            q.append(" and createdAt >= :from");
            params.put("from", from);
        }
        if (to != null) {
            q.append(" and createdAt <= :to");
            params.put("to", to);
        }

        return find(q.toString() + " order by createdAt desc", params)
                .page(Page.of(page, size))
                .list();
    }
}
