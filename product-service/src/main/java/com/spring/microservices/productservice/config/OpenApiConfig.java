package com.spring.microservices.productservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServicesAPI(){
        return new OpenAPI()
                .info(new Info().title("Product Service API")
                        .description("REST APIs for product service.")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Refer to product service wiki documentation for more information.")
                        .url("https://en.wikipedia.org/wiki/Product-service_system"));
    }
}
