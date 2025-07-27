//package com.example.casestudy.request_service.configuration;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//
//@Configuration
//public class OpenAPIConfig {
//    @Bean
//    public OpenAPI requestServiceAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Seat Request Service API")
//                        .description("Service for managing seat requests")
//                        .version("1.0"))
//                .servers(Arrays.asList(
//                        new Server().url("http://localhost:8080").description("Local server")
//                ));
//    }
//}