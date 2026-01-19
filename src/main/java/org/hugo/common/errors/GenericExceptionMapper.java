package org.hugo.common.errors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.hugo.common.logging.AppLog;
import org.hugo.common.logging.AppLogger;
import org.hugo.common.response.ApiResponse;

import java.util.Map;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final AppLogger LOG = AppLog.get(GenericExceptionMapper.class);

    private enum LogLevel { DEBUG, WARN, ERROR }

    private static final Map<Integer, LogLevel> WEB_STATUS_TO_LEVEL = Map.of(
            404, LogLevel.DEBUG,
            400, LogLevel.WARN,
            401, LogLevel.WARN,
            403, LogLevel.WARN,
            409, LogLevel.WARN
    );

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable ex) {
        String path = uriInfo != null ? uriInfo.getPath() : null;

        if (ex instanceof IllegalArgumentException iae) {
            LOG.debug("Bad request (IllegalArgumentException): path=%s msg=%s", path, iae.getMessage());
            ApiError err = ApiError.of("BAD_REQUEST", iae.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(err, path))
                    .build();
        }

        if (ex instanceof WebApplicationException wae) {
            int status = wae.getResponse().getStatus();
            String msg = wae.getMessage();

            LogLevel level = WEB_STATUS_TO_LEVEL.getOrDefault(
                    status,
                    (status >= 500 ? LogLevel.ERROR : LogLevel.WARN)
            );

            if (level == LogLevel.DEBUG) {
                LOG.debug("WebApplicationException: status=%d path=%s msg=%s", status, path, msg);
            } else if (level == LogLevel.WARN) {
                LOG.warn("WebApplicationException: status=%d path=%s msg=%s", status, path, msg);
            } else {
                // 5xx: con stacktrace
                LOG.error(wae, "WebApplicationException 5xx: status=%d path=%s", status, path);
            }

            ApiError err = ApiError.of("HTTP_" + status, msg);
            return Response.status(status)
                    .entity(ApiResponse.error(err, path))
                    .build();
        }

        LOG.error(ex, "Unhandled exception: path=%s type=%s", path, ex.getClass().getName());

        ApiError err = ApiError.of("INTERNAL_ERROR", "Unexpected error");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error(err, path))
                .build();
    }
}
