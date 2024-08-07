package co.udea.codefact.config;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI swagger() {

        Info info = new Info()
                .title("Codefact tutoring")
                .version("1.0")
                .description("API for Codefact tutoring");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
               .addList("bearerAuth");

        return new OpenAPI()
                .info(info)
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);

    }
}