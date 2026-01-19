package org.hugo.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.Collectors;

import org.hugo.common.errors.ApiError;
import org.hugo.common.errors.ApiErrorDetail;
import org.hugo.common.response.ApiResponse;


@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        String path = uriInfo != null ? uriInfo.getPath() : null;

        List<ApiErrorDetail> details = ex.getConstraintViolations()
                .stream()
                .map(this::toDetail)
                .collect(Collectors.toList());

        ApiError err = ApiError.of("BAD_REQUEST", "Validation failed", details);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.error(err, path))
                .build();
    }

    private ApiErrorDetail toDetail(ConstraintViolation<?> v) {

        String field = v.getPropertyPath() != null ? v.getPropertyPath().toString() : "unknown";
        String issue = v.getMessage();

        return ApiErrorDetail.of(field, issue);
    }
}
