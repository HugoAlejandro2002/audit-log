package org.hugo.common.logging;

public final class AppLog {
    private AppLog() {}
    public static AppLogger get(Class<?> clazz) {
        return new JbossAppLogger(clazz);
    }
}
