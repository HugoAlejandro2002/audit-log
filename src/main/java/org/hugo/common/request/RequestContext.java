package org.hugo.common.request;

public final class RequestContext {
    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> ACTOR_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> PATH = new ThreadLocal<>();

    private RequestContext() {}

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    public static void setActorId(String actorId) {
        ACTOR_ID.set(actorId);
    }

    public static String getActorId() {
        return ACTOR_ID.get();
    }

    public static void setPath(String path) {
        PATH.set(path);
    }

    public static String getPath() {
        return PATH.get();
    }

    public static void clear() {
        REQUEST_ID.remove();
        ACTOR_ID.remove();
        PATH.remove();
    }
}