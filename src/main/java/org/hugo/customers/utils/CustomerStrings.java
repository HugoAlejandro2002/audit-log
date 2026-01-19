package org.hugo.customers.utils;

public class CustomerStrings {
    private CustomerStrings() {}

    public static String normalizeName(String name) {
        return name == null ? null : name.trim();
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
