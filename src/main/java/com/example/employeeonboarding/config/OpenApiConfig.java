package com.example.employeeonboarding.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeOnboardingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Onboarding API")
                        .version("1.0")
                        .description("REST API for managing employee onboarding records"));
    }
}
