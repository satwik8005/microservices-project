package com.example.casestudy.approval_service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI approvalServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Seat Approval Service API")
                        .description("Service for managing seat request approvals")
                        .version("1.0"))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8083").description("Local server")
                ));
    }
}