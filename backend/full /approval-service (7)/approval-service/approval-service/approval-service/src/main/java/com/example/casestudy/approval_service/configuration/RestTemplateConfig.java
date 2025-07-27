package com.example.casestudy.approval_service.configuration;

//import com.example.casestudy.approval_service.service.AuthorizationHeaderInterceptor;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add interceptor for forwarding authorization headers
        restTemplate.getInterceptors().add((request, body, execution) -> {
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                String authHeader = requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
                if (authHeader != null) {
                    request.getHeaders().set(HttpHeaders.AUTHORIZATION, authHeader);
                    request.getHeaders().set("X-Forwarded-Authorization", authHeader);
                }
            }
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}