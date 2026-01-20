package org.hugo.common.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ApiError", description = "Descripción de error estándar")
public class ApiError {
    @Schema(description = "Código de error", example = "BAD_REQUEST")
    public String code;
    @Schema(description = "Mensaje legible del error", example = "Validation failed")
    public String message;
    @Schema(description = "Lista de detalles de validación u otros problemas")
    public List<ApiErrorDetail> details;

    public static ApiError of(String code, String message) {
        ApiError e = new ApiError();
        e.code = code;
        e.message = message;
        return e;
    }

    public static ApiError of(String code, String message, List<ApiErrorDetail> details) {
        ApiError e = of(code, message);
        e.details = details;
        return e;
    }
}
