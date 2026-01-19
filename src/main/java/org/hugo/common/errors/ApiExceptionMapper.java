package org.hugo.common.errors;

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
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    private static final AppLogger LOG = AppLog.get(ApiExceptionMapper.class);

    // Mapping status -> nivel log (enterprise: 400/404 suelen ser ruido; 409 útil)
    private enum LogLevel { DEBUG, WARN }

    private static final Map<Integer, LogLevel> STATUS_TO_LEVEL = Map.of(
            400, LogLevel.DEBUG,
            404, LogLevel.DEBUG,
            409, LogLevel.WARN
    );

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ApiException ex) {
        String path = uriInfo != null ? uriInfo.getPath() : null;

        int status = ex.getStatus().getStatusCode();
        String code = ex.getCode();
        String msg = ex.getMessage();

        LogLevel level = STATUS_TO_LEVEL.getOrDefault(status, LogLevel.WARN);

        if (level == LogLevel.DEBUG) {
            LOG.debug("Handled ApiException: status=%d code=%s path=%s msg=%s", status, code, path, msg);
        } else {
            LOG.warn("Handled ApiException: status=%d code=%s path=%s msg=%s", status, code, path, msg);
        }

        ApiError err = ApiError.of(code, msg);
        return Response.status(ex.getStatus())
                .entity(ApiResponse.error(err, path))
                .build();
    }
}
