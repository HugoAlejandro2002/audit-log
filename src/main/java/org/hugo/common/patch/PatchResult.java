package org.hugo.common.patch;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public record PatchResult(
        Set<String> changedFields,
        Map<String, Object> before,
        Map<String, Object> after
) {
    public static PatchResult empty() {
        return new PatchResult(Set.of(), Map.of(), Map.of());
    }

    public PatchResult {
        changedFields = Collections.unmodifiableSet(changedFields);
        before = Collections.unmodifiableMap(before);
        after = Collections.unmodifiableMap(after);
    }

    public boolean changed() {
        return !changedFields.isEmpty();
    }
}
