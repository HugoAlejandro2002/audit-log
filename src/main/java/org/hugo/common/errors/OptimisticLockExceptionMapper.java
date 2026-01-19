package org.hugo.common.errors;

import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.hugo.common.logging.AppLog;
import org.hugo.common.logging.AppLogger;
import org.hugo.common.response.ApiResponse;

@Provider
public class OptimisticLockExceptionMapper implements ExceptionMapper<OptimisticLockException> {

    private static final AppLogger LOG = AppLog.get(OptimisticLockExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(OptimisticLockException ex) {
        String path = uriInfo != null ? uriInfo.getPath() : null;

        LOG.warn("OptimisticLockException -> 409: path=%s msg=%s", path, ex.getMessage());

        ApiError err = ApiError.of(
                "CONCURRENT_MODIFICATION",
                "Concurrent modification. Please retry."
        );

        return Response.status(Response.Status.CONFLICT)
                .entity(ApiResponse.error(err, path))
                .build();
    }
}
