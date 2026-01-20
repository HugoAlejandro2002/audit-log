package org.hugo.common.errors;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ApiErrorDetail", description = "Detalle de error por campo o restricción")
public class ApiErrorDetail {
    @Schema(description = "Campo o propiedad relacionada", example = "page")
    public String field;
    @Schema(description = "Motivo del problema", example = "page must be >= 0")
    public String issue;

    public static ApiErrorDetail of(String field, String issue) {
        ApiErrorDetail d = new ApiErrorDetail();
        d.field = field;
        d.issue = issue;
        return d;
    }
}
