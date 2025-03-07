package org.example.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BAD_541")
                        .version("1.0")
                        .description("API Documentation for the Project")
                        .contact(new Contact().name("Gon Chaung").email("gonChaung.dev@gmail.com")));
    }
}