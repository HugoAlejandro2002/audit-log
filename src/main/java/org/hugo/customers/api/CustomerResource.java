package org.hugo.customers.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.BeanParam;

import org.hugo.common.logging.AppLog;
import org.hugo.common.logging.AppLogger;
import org.hugo.common.response.ApiResponse;
import org.hugo.customers.dto.CreateCustomerRequest;
import org.hugo.customers.dto.CustomerResponse;
import org.hugo.customers.dto.UpdateCustomerRequest;
import org.hugo.customers.dto.params.CustomerIdParam;
import org.hugo.customers.dto.params.CustomerListParams;
import org.hugo.customers.services.CustomerService;

import java.util.List;

@Path("/customers")
@Consumes("application/json")
@Produces("application/json")
public class CustomerResource {

    private static final AppLogger LOG = AppLog.get(CustomerResource.class);

    private final CustomerService service;

    public CustomerResource(CustomerService service) {
        this.service = service;
    }

    // ================= CREATE =================

    @POST
    public ApiResponse<CustomerResponse> create(
            @Valid CreateCustomerRequest req,
            @Context UriInfo uriInfo
    ) {
        String path = uriInfo.getPath();

        LOG.info(
                "CustomerResource.create - path=%s namePresent=%s emailPresent=%s",
                path,
                req.name() != null,
                req.email() != null
        );

        CustomerResponse created = service.create(req);

        LOG.info(
                "CustomerResource.create - created customerId=%d path=%s",
                created.id(),
                path
        );

        return ApiResponse.ok(created, path);
    }

    @GET
    public ApiResponse<List<CustomerResponse>> list(
            @Valid @BeanParam CustomerListParams p,
            @Context UriInfo uriInfo
    ) {
        String path = uriInfo.getPath();

        LOG.debug(
                "CustomerResource.list - path=%s page=%d size=%d activeOnly=%s",
                path, p.page, p.size, p.activeOnly
        );

        List<CustomerResponse> data = service.list(p.page, p.size, p.activeOnly);

        LOG.debug(
                "CustomerResource.list - resultCount=%d path=%s",
                data.size(), path
        );

        return ApiResponse.ok(data, path);
    }

    // ================= GET =================

    @GET
    @Path("/{id}")
    public ApiResponse<CustomerResponse> get(
            @Valid @BeanParam CustomerIdParam p,
            @Context UriInfo uriInfo
    ) {
        String path = uriInfo.getPath();

        LOG.debug(
                "CustomerResource.get - id=%d path=%s",
                p.id, path
        );

        CustomerResponse data = service.getById(p.id);

        LOG.debug(
                "CustomerResource.get - found customerId=%d path=%s",
                data.id(), path
        );

        return ApiResponse.ok(data, path);
    }

    // ================= UPDATE =================

    @PUT
    @Path("/{id}")
    public ApiResponse<CustomerResponse> update(
            @Valid @BeanParam CustomerIdParam p,
            @Valid UpdateCustomerRequest req,
            @Context UriInfo uriInfo
    ) {
        String path = uriInfo.getPath();

        LOG.info(
                "CustomerResource.update - id=%d path=%s fields[name=%s,email=%s,active=%s]",
                p.id,
                path,
                req.name() != null,
                req.email() != null,
                req.active() != null
        );

        CustomerResponse updated = service.update(p.id, req);

        LOG.info(
                "CustomerResource.update - updated customerId=%d path=%s",
                updated.id(), path
        );

        return ApiResponse.ok(updated, path);
    }

    // ================= DELETE =================

    @DELETE
    @Path("/{id}")
    public ApiResponse<Void> delete(
            @Valid @BeanParam CustomerIdParam p,
            @Context UriInfo uriInfo
    ) {
        String path = uriInfo.getPath();

        LOG.info(
                "CustomerResource.delete - id=%d path=%s",
                p.id, path
        );

        service.delete(p.id);

        LOG.info(
                "CustomerResource.delete - done id=%d path=%s",
                p.id, path
        );

        return ApiResponse.ok(path);
    }
}
