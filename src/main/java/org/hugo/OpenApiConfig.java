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
                description = "API for querying audit events and managing customers. All responses are wrapped in the ApiResponse object.",
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "/api/v1", description = "API base path")
        },
        tags = {
                @Tag(name = "Audit", description = "Audit operations"),
                @Tag(name = "Customers", description = "Customer operations")
        }
)
public class OpenApiConfig {
}
