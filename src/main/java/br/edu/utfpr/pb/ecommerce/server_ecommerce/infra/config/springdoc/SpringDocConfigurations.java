package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.config.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("RiffHouse Ecommerce API")
                        .description("REST API for the RiffHouse E-commerce platform, providing functionalities for managing products, orders, users, and more.")
                        .contact(new Contact()
                                .name("Lucas Matheus de Camargo")
                                .email("decamargoluk@gmail.com")
                                .url("https://www.linkedin.com/in/lucas-matheus-de-camargo-49a315236/")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080").description("Local development server"),
                        new Server()
                                .url("https://riffhouse-api.onrender.com").description("Production server")
                ));
    }
}
