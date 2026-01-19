package org.hugo.customers.dto.params;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.PathParam;

public class CustomerIdParam {
    @PathParam("id")
    @Min(value = 1, message = "id must be >= 1")
    public long id;
}
