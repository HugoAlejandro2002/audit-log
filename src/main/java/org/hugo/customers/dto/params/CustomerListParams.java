package org.hugo.customers.dto.params;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public class CustomerListParams {

    @QueryParam("page")
    @DefaultValue("0")
    @Min(value = 0, message = "page must be >= 0")
    public int page;

    @QueryParam("size")
    @DefaultValue("20")
    @Min(value = 1, message = "size must be between 1 and 100")
    @Max(value = 100, message = "size must be between 1 and 100")
    public int size;

    @QueryParam("activeOnly")
    @DefaultValue("true")
    public boolean activeOnly;
}
