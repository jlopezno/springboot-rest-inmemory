package com.jorge.inventoryapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory API")
                        .description("REST API for users, products, inventory movements, validations and H2 persistence with Spring Boot.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jorge")));
    }
}
