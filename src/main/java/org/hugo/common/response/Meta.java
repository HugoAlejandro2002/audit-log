package org.hugo.common.response;

import org.hugo.common.request.RequestContext;

import java.time.Instant;

public class Meta {
    public String requestId;
    public Instant timestamp;
    public String path;

    public static Meta of(String path) {
        Meta m = new Meta();
        m.requestId = RequestContext.getRequestId();
        m.timestamp = Instant.now();
        m.path = path;
        return m;
    }
}