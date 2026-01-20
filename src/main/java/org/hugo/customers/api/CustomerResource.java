package org.hugo.customers.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.BeanParam;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.hugo.common.logging.AppLog;
import org.hugo.common.logging.AppLogger;
import org.hugo.common.response.ApiResponse;
import org.hugo.customers.dto.CreateCustomerRequest;
import org.hugo.customers.dto.CreateCustomerForm;
import org.hugo.customers.dto.CustomerResponse;
import org.hugo.customers.dto.UpdateCustomerRequest;
import org.hugo.customers.dto.UpdateCustomerForm;
import org.hugo.customers.dto.params.CustomerIdParam;
import org.hugo.customers.dto.params.CustomerListParams;
import org.hugo.customers.services.CustomerService;

import java.util.List;

@Path("/customers")
@Consumes("application/json")
@Produces("application/json")
@Tag(name = "Customers", description = "API for customer management")
public class CustomerResource {

    private static final AppLogger LOG = AppLog.get(CustomerResource.class);

    private final CustomerService service;

    public CustomerResource(CustomerService service) {
        this.service = service;
    }

    // ================= CREATE =================

    @POST
    @Consumes("application/json")
    @Operation(
            summary = "Create customer",
            description = "Creates a new customer. Returns the created customer."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customer created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "OK", value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "id": 42,
                                                "name": "Ada Lovelace",
                                                "email": "ada@example.com",
                                                "active": true,
                                                "createdAt": "2025-01-01T10:00:00Z",
                                                "updatedAt": "2025-01-01T10:00:00Z",
                                                "version": 1
                                              },
                                              "meta": {
                                                "requestId": "req-aaa",
                                                "timestamp": "2025-01-01T10:00:00Z",
                                                "path": "/api/v1/customers"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "BadRequest", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "BAD_REQUEST",
                                                "message": "Validation failed",
                                                "details": [
                                                  {"field": "name", "issue": "name is required"},
                                                  {"field": "email", "issue": "email must be valid"}
                                                ]
                                              },
                                              "meta": {
                                                "requestId": "req-aaa",
                                                "timestamp": "2025-01-01T10:00:00Z",
                                                "path": "/api/v1/customers"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "Conflict", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "CONFLICT",
                                                "message": "email already exists"
                                              },
                                              "meta": {
                                                "requestId": "req-aaa",
                                                "timestamp": "2025-01-01T10:00:00Z",
                                                "path": "/api/v1/customers"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "InternalError", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INTERNAL_ERROR",
                                                "message": "Unexpected error"
                                              },
                                              "meta": {
                                                "requestId": "req-aaa",
                                                "timestamp": "2025-01-01T10:00:00Z",
                                                "path": "/api/v1/customers"
                                              }
                                            }
                                            """)
                            }
                    )
            )
    })
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

    @POST
    @Consumes("multipart/form-data")
    @Operation(
            summary = "Create customer (multipart)",
            description = "Creates a new customer using multipart form."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customer created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @APIResponse(responseCode = "400", description = "Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @APIResponse(responseCode = "409", description = "Email already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @APIResponse(responseCode = "500", description = "Internal error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<CustomerResponse> createMultipart(
            @org.jboss.resteasy.reactive.MultipartForm CreateCustomerForm form,
            @Context UriInfo uriInfo
    ) {
        String path = uriInfo.getPath();
        LOG.info("CustomerResource.createMultipart - path=%s namePresent=%s emailPresent=%s",
                path,
                form.name != null,
                form.email != null
        );
        CreateCustomerRequest req = new CreateCustomerRequest(form.name, form.email);
        CustomerResponse created = service.create(req);
        return ApiResponse.ok(created, path);
    }

    @GET
    @Operation(
            summary = "List customers",
            description = "Lists customers with pagination, optionally filtering by active status."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customers list",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "OK", value = """
                                            {
                                              "success": true,
                                              "data": [
                                                {
                                                  "id": 42,
                                                  "name": "Ada Lovelace",
                                                  "email": "ada@example.com",
                                                  "active": true,
                                                  "createdAt": "2025-01-01T10:00:00Z",
                                                  "updatedAt": "2025-01-01T10:00:00Z",
                                                  "version": 1
                                                }
                                              ],
                                              "meta": {
                                                "requestId": "req-bbb",
                                                "timestamp": "2025-01-01T10:00:00Z",
                                                "path": "/api/v1/customers"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                responseCode = "400",
                description = "Invalid parameters",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = {
                                @ExampleObject(name = "BadRequest", value = """
                                        {
                                          "success": false,
                                          "error": {
                                            "code": "BAD_REQUEST",
                                            "message": "Validation failed",
                                            "details": [
                                              {"field": "page", "issue": "page must be >= 0"}
                                            ]
                                          },
                                          "meta": {
                                            "requestId": "req-bbb",
                                            "timestamp": "2025-01-01T10:00:00Z",
                                            "path": "/api/v1/customers"
                                          }
                                        }
                                        """)
                        }
                )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
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
    @Operation(
            summary = "Get customer by ID",
            description = "Gets the customer by its ID."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customer found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "OK", value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "id": 42,
                                                "name": "Ada Lovelace",
                                                "email": "ada@example.com",
                                                "active": true,
                                                "createdAt": "2025-01-01T10:00:00Z",
                                                "updatedAt": "2025-01-01T10:00:00Z",
                                                "version": 1
                                              },
                                              "meta": {
                                                "requestId": "req-ccc",
                                                "timestamp": "2025-01-01T10:00:00Z",
                                                "path": "/api/v1/customers/42"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "NotFound", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "NOT_FOUND",
                                                "message": "customer not found"
                                              },
                                              "meta": {
                                                "requestId": "req-ccc",
                                                "timestamp": "2025-01-01T10:00:00Z",
                                                "path": "/api/v1/customers/0"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
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
    @Consumes("application/json")
    @Operation(
            summary = "Update customer",
            description = "Updates customer fields. May return 409 if the email already exists."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customer updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "OK", value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "id": 42,
                                                "name": "Ada Byron",
                                                "email": "ada.byron@example.com",
                                                "active": true,
                                                "createdAt": "2025-01-01T10:00:00Z",
                                                "updatedAt": "2025-01-05T11:00:00Z",
                                                "version": 2
                                              },
                                              "meta": {
                                                "requestId": "req-ddd",
                                                "timestamp": "2025-01-05T11:00:00Z",
                                                "path": "/api/v1/customers/42"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "Conflict", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "CONFLICT",
                                                "message": "email already exists"
                                              },
                                              "meta": {
                                                "requestId": "req-ddd",
                                                "timestamp": "2025-01-05T11:00:00Z",
                                                "path": "/api/v1/customers/42"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
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

    @PUT
    @Path("/{id}")
    @Consumes("multipart/form-data")
    @Operation(
            summary = "Update customer (multipart)",
            description = "Updates customer fields using multipart form."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Customer updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @APIResponse(responseCode = "400", description = "Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @APIResponse(responseCode = "404", description = "Customer not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @APIResponse(responseCode = "409", description = "Email already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @APIResponse(responseCode = "500", description = "Internal error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<CustomerResponse> updateMultipart(
            @Valid @BeanParam CustomerIdParam p,
            @org.jboss.resteasy.reactive.MultipartForm UpdateCustomerForm form,
            @Context UriInfo uriInfo
    ) {
        String path = uriInfo.getPath();
        LOG.info("CustomerResource.updateMultipart - id=%d path=%s fields[name=%s,email=%s,active=%s]",
                p.id,
                path,
                form.name != null,
                form.email != null,
                form.active != null
        );
        UpdateCustomerRequest req = new UpdateCustomerRequest(form.name, form.email, form.active);
        CustomerResponse updated = service.update(p.id, req);
        return ApiResponse.ok(updated, path);
    }

    // ================= DELETE =================

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete customer",
            description = "Deletes the customer by ID. Successful response without data body."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "OK", value = """
                                            {
                                              "success": true,
                                              "data": null,
                                              "meta": {
                                                "requestId": "req-eee",
                                                "timestamp": "2025-01-06T11:00:00Z",
                                                "path": "/api/v1/customers/42"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
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
