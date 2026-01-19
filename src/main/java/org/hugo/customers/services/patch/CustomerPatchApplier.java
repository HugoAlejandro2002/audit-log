package org.hugo.customers.services.patch;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.hugo.common.errors.ApiException;
import org.hugo.common.patch.PatchApplier;
import org.hugo.common.patch.PatchResult;
import org.hugo.customers.domain.Customer;
import org.hugo.customers.dto.UpdateCustomerRequest;
import org.hugo.customers.utils.CustomerStrings;

@ApplicationScoped
public class CustomerPatchApplier implements PatchApplier<Customer, UpdateCustomerRequest> {

    @Override
    public PatchResult apply(Customer c, UpdateCustomerRequest req) {
        Set<String> changed = new LinkedHashSet<>();
        Map<String, Object> before = new LinkedHashMap<>();
        Map<String, Object> after = new LinkedHashMap<>();

        // name
        if (req.name() != null) {
            String newName = CustomerStrings.normalizeName(req.name());
            if (newName.isBlank()) throw ApiException.badRequest("name cannot be blank");

            if (!newName.equals(c.name)) {
                changed.add("name");
                before.put("name", c.name);
                after.put("name", newName);
                c.name = newName;
            }
        }

        // email
        if (req.email() != null) {
            String newEmail = CustomerStrings.normalizeEmail(req.email());
            if (newEmail.isBlank()) throw ApiException.badRequest("email cannot be blank");

            if (!newEmail.equals(c.email)) {
                changed.add("email");
                before.put("email", c.email);
                after.put("email", newEmail);
                c.email = newEmail;
            }
        }

        // active
        if (req.active() != null) {
            boolean newActive = req.active();
            if (newActive != c.active) {
                changed.add("active");
                before.put("active", c.active);
                after.put("active", newActive);
                c.active = newActive;
            }
        }

        if (changed.isEmpty()) return PatchResult.empty();
        return new PatchResult(changed, before, after);
    }
}
