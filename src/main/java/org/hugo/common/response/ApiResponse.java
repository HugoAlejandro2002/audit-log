package org.hugo.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hugo.common.errors.ApiError;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ApiResponse", description = "Envoltorio estándar para todas las respuestas de la API")
public class ApiResponse<T> {
    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    public boolean success;
    @Schema(description = "Datos de la respuesta. Puede ser objeto o lista")
    public T data;
    @Schema(description = "Información de error cuando success=false")
    public ApiError error;
    @Schema(description = "Metadatos de la respuesta (requestId, timestamp, path)")
    public Meta meta;

    public static <T> ApiResponse<T> ok(T data, String path) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.data = data;
        r.meta = Meta.of(path);
        return r;
    }

    public static ApiResponse<Void> ok(String path) {
        return ok(null, path);
    }

    public static ApiResponse<Void> error(ApiError error, String path) {
        ApiResponse<Void> r = new ApiResponse<>();
        r.success = false;
        r.error = error;
        r.meta = Meta.of(path);
        return r;
    }
}
