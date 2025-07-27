package com.casestudy.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/auth-service/auth/register",
            "/auth-service/auth/authenticate",
            "/auth-service/auth/login",
            "/auth-service/auth/getEmployee/**",
            "/api/seat-request/getAllSeats",
            "/api/seat-request/getAllSeatStatus",
            "api/seat-request//getSeatForManager",
            "/api/seat-approval/pendingSeatForApproval",
            "/api/seat-approval/**",
            "/api/seat-request/getRequest/{id}",
            "/api/seat-request/getRequest/**",
            "/api/seat-approval/pendingRequestByManager"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}