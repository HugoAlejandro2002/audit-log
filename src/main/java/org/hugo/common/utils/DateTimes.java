package org.hugo.common.utils;

import org.hugo.common.errors.ApiException;

import java.time.Instant;

public final class DateTimes {
    private DateTimes() {}

    public static Instant parseInstantOrNull(String raw, String fieldName) {
        raw = Strings.trimToNull(raw);
        if (raw == null) return null;
        try {
            return Instant.parse(raw);
        } catch (Exception e) {
            throw ApiException.badRequest(fieldName + " must be ISO-8601, e.g. 2026-01-01T00:00:00Z");
        }
    }
}
