package org.market_mangement.market_management_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Market Management API",
                description = "REST endpoints for markets, shops, billing, reporting, and utilities",
                version = "v1",
                contact = @Contact(name = "Market Management Team", email = "support@example.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local")
        }
)
public class OpenApiConfig {
    // Declarative configuration for OpenAPI/Swagger metadata
}

