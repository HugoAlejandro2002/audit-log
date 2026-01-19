package org.hugo.common.request;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.MDC;

import java.io.IOException;
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class RequestContextFilter
        implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String HEADER_REQUEST_ID = "X-Request-Id";
    public static final String HEADER_ACTOR_ID = "X-Actor-Id";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String requestId = requestContext.getHeaderString(HEADER_REQUEST_ID);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        String actorId = requestContext.getHeaderString(HEADER_ACTOR_ID);

        RequestContext.setRequestId(requestId);
        RequestContext.setActorId(actorId);
        RequestContext.setPath(requestContext.getUriInfo().getPath());

        MDC.put("requestId", requestId);
        MDC.put("path", requestContext.getUriInfo().getPath());

        if (actorId != null && !actorId.isBlank()) {
            MDC.put("actorId", actorId);
        }
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException {

        String requestId = RequestContext.getRequestId();
        if (requestId != null) {
            responseContext.getHeaders()
                    .putSingle(HEADER_REQUEST_ID, requestId);
        }

        String actorId = RequestContext.getActorId();
        if (actorId != null && !actorId.isBlank()) {
            responseContext.getHeaders()
                    .putSingle(HEADER_ACTOR_ID, actorId);
        }

        RequestContext.clear();
        MDC.clear();
    }
}
