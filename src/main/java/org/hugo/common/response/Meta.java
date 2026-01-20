package org.hugo.common.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hugo.common.request.RequestContext;

import java.time.Instant;

@Schema(name = "Meta", description = "Metadatos para rastreo de la respuesta")
public class Meta {
    @Schema(description = "ID del request", example = "req-5b0c1e")
    public String requestId;
    @Schema(description = "Instante de generación de la respuesta (UTC)", example = "2025-09-23T12:34:56Z")
    public Instant timestamp;
    @Schema(description = "Ruta solicitada", example = "/api/v1/audit")
    public String path;

    public static Meta of(String path) {
        Meta m = new Meta();
        m.requestId = RequestContext.getRequestId();
        m.timestamp = Instant.now();
        m.path = path;
        return m;
    }
}
