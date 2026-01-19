package org.hugo.common.logging;

import org.jboss.logging.Logger;

final class JbossAppLogger implements AppLogger {
    private final Logger logger;

    JbossAppLogger(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz);
    }

    @Override public void debug(String msg, Object... args) { if (logger.isDebugEnabled()) logger.debugf(msg, args); }
    @Override public void info(String msg, Object... args) { logger.infof(msg, args); }
    @Override public void warn(String msg, Object... args) { logger.warnf(msg, args); }
    @Override public void error(Throwable t, String msg, Object... args) { logger.errorf(t, msg, args); }
    @Override public boolean isDebugEnabled() { return logger.isDebugEnabled(); }
}
