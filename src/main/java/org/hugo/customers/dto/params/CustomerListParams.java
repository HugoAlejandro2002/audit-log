package org.hugo.customers.dto.params;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

public class CustomerListParams {

    @QueryParam("page")
    @DefaultValue("0")
    @Min(value = 0, message = "page must be >= 0")
    @Parameter(name = "page", in = ParameterIn.QUERY, description = "Número de página (0-index)", example = "0")
    @Schema(minimum = "0")
    public int page;

    @QueryParam("size")
    @DefaultValue("20")
    @Min(value = 1, message = "size must be between 1 and 100")
    @Max(value = 100, message = "size must be between 1 and 100")
    @Parameter(name = "size", in = ParameterIn.QUERY, description = "Tamaño de página (1-100)", example = "20")
    @Schema(minimum = "1", maximum = "100")
    public int size;

    @QueryParam("activeOnly")
    @DefaultValue("true")
    @Parameter(name = "activeOnly", in = ParameterIn.QUERY, description = "Filtrar solo clientes activos", example = "true")
    public boolean activeOnly;
}
