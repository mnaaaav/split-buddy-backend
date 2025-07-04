package com.splitbuddy.settlement_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Settlement Service API")
                .description("API documentation for Settlement Service")
                .version("1.0.0")
                .contact(new Contact()
                    .name("SplitBuddy Support")
                    .email("support@splitbuddy.com")));
    }
}
