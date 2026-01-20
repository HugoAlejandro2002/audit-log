package org.hugo;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "Audit Log API",
                version = "1.0.0",
                description = "API para consulta de eventos de auditoría y gestión de clientes. Todas las respuestas están envueltas en el objeto ApiResponse.",
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "/api/v1", description = "Base path de la API")
        },
        tags = {
                @Tag(name = "Audit", description = "Operaciones de auditoría"),
                @Tag(name = "Customers", description = "Operaciones sobre clientes")
        }
)
public class OpenApiConfig {
}
