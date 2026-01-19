package org.hugo.common.utils;

public final class Strings {
    private Strings() {}

    public static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
