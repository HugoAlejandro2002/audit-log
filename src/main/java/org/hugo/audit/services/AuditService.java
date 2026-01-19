package org.hugo.audit.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.hugo.audit.domain.*;
import org.hugo.audit.dto.AuditEventResponse;
import org.hugo.audit.dto.params.AuditListParams;
import org.hugo.audit.repositories.AuditEventRepository;
import org.hugo.common.errors.ApiException;
import org.hugo.common.logging.AppLog;
import org.hugo.common.logging.AppLogger;
import org.hugo.common.request.RequestContext;
import org.hugo.common.utils.Strings;
import org.hugo.common.utils.DateTimes;

import java.time.Instant;
import java.util.List;


@ApplicationScoped
public class AuditService {

    private static final AppLogger LOG = AppLog.get(AuditService.class);
    private final AuditEventRepository repo;

    public AuditService(AuditEventRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void record(
            AuditEntityType entityType,
            String entityId,
            AuditAction action,
            String changesJson,
            String beforeJson,
            String afterJson
    ) {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.entityType = entityType;
        auditEvent.entityId = entityId;
        auditEvent.action = action;

        auditEvent.actorId = RequestContext.getActorId();
        auditEvent.requestId = RequestContext.getRequestId();
        auditEvent.path = RequestContext.getPath();

        auditEvent.changesJson = changesJson;
        auditEvent.beforeJson = beforeJson;
        auditEvent.afterJson = afterJson;

        repo.persist(auditEvent);
    }

    public AuditEventResponse getById(long id) {
        AuditEvent e = repo.findById(id);
        if (e == null) return null;
        return AuditEventResponse.from(e);
    }

    public List<AuditEventResponse> list(int page, int size) {
        return repo.listAllPaged(page, size)
                .stream()
                .map(AuditEventResponse::from)
                .toList();
    }

    public List<AuditEventResponse> list(AuditListParams p) {
        String entityType = Strings.trimToNull(p.entityType);
        String entityId   = Strings.trimToNull(p.entityId);
        String requestId  = Strings.trimToNull(p.requestId);
        String actorId    = Strings.trimToNull(p.actorId);

        Instant from = DateTimes.parseInstantOrNull(p.from, "from");
        Instant to   = DateTimes.parseInstantOrNull(p.to, "to");

        if (from != null && to != null && from.isAfter(to)) {
            throw ApiException.badRequest("'from' must be <= 'to'");
        }

        LOG.info("AuditService.list page={} size={} entityType={} entityId={} requestIdPresent={} actorIdPresent={} from={} to={}",
                p.page, p.size, entityType, entityId,
                requestId != null, actorId != null, from, to);

        return repo.listFiltered(p.page, p.size, entityType, entityId, requestId, actorId, from, to)
                .stream()
                .map(AuditEventResponse::from)
                .toList();
    }
}
