package org.hugo.customers.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.hugo.audit.domain.AuditAction;
import org.hugo.audit.domain.AuditEntityType;
import org.hugo.audit.services.AuditService;
import org.hugo.common.errors.ApiException;
import org.hugo.common.patch.PatchResult;
import org.hugo.common.utils.JsonUtils;
import org.hugo.customers.domain.Customer;
import org.hugo.customers.dto.CreateCustomerRequest;
import org.hugo.customers.dto.CustomerResponse;
import org.hugo.customers.dto.UpdateCustomerRequest;
import org.hugo.customers.repositories.CustomerRepository;
import org.hugo.customers.services.patch.CustomerPatchApplier;
import org.hugo.customers.utils.CustomerStrings;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerPatchApplier patchApplier;
    private final AuditService auditService;
    private final JsonUtils jsonUtils;

    public CustomerService(
            CustomerRepository repo,
            CustomerPatchApplier patchApplier,
            AuditService auditService,
            JsonUtils jsonUtils
    ) {
        this.repo = repo;
        this.patchApplier = patchApplier;
        this.auditService = auditService;
        this.jsonUtils = jsonUtils;
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest req) {
        String email = CustomerStrings.normalizeEmail(req.email());

        if (repo.existsByEmail(email)) {
            throw ApiException.conflict("email already exists");
        }

        Customer c = new Customer();
        c.name = CustomerStrings.normalizeName(req.name());
        c.email = email;

        repo.persist(c);

        CustomerResponse customerResponse = CustomerResponse.from(c);

        auditService.record(
                AuditEntityType.CUSTOMER,
                String.valueOf(c.id),
                AuditAction.CREATE,
                null,
                null,
                jsonUtils.toJson(customerResponse)
        );

        return customerResponse;
    }

    public CustomerResponse getById(long id) {
        Customer c = repo.findById(id);
        if (c == null) throw ApiException.notFound("customer not found");
        return CustomerResponse.from(c);
    }

    public List<CustomerResponse> list(int page, int size, boolean activeOnly) {
        List<Customer> customers = activeOnly
                ? repo.listActivePage(page, size)
                : repo.listPage(page, size);

        return customers.stream()
                .map(CustomerResponse::from)
                .toList();
    }

    @Transactional
    public CustomerResponse update(long id, UpdateCustomerRequest req) {
        Customer c = repo.findById(id);
        if (c == null) throw ApiException.notFound("customer not found");

        PatchResult patch = patchApplier.apply(c, req);

        if (!patch.changed()) {
            return CustomerResponse.from(c);
        }

        if (patch.changedFields().contains("email")) {
            if (repo.existsByEmailAndIdNot(c.email, c.id)) {
                throw ApiException.conflict("email already exists");
            }
        }

        auditService.record(
                AuditEntityType.CUSTOMER,
                String.valueOf(c.id),
                AuditAction.UPDATE,
                jsonUtils.toJson(patch.after()),
                jsonUtils.toJson(patch.before()),
                jsonUtils.toJson(patch.after())
        );

        return CustomerResponse.from(c);
    }

    @Transactional
    public void delete(long id) {
        Customer c = repo.findById(id);
        if (c == null) throw ApiException.notFound("customer not found");

        boolean beforeActive = c.active;
        c.active = false;

        auditService.record(
                AuditEntityType.CUSTOMER,
                String.valueOf(c.id),
                AuditAction.DELETE,
                jsonUtils.toJson(Map.of("active", false)),
                jsonUtils.toJson(Map.of("active", beforeActive)),
                jsonUtils.toJson(Map.of("active", false))
        );
    }
}
