package org.hugo.common.logging;

public interface AppLogger {
    void debug(String msg, Object... args);
    void info(String msg, Object... args);
    void warn(String msg, Object... args);
    void error(Throwable t, String msg, Object... args);
    boolean isDebugEnabled();
}
