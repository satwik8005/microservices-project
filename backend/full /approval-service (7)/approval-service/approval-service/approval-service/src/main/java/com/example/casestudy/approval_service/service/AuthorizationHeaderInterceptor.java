//package com.example.casestudy.approval_service.service;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {
//
//    private final HttpServletRequest httpServletRequest;
//
//    public AuthorizationHeaderInterceptor(HttpServletRequest httpServletRequest) {
//        this.httpServletRequest = httpServletRequest;
//    }
//
//    @Override
//    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
//                                        ClientHttpRequestExecution execution) throws IOException {
//
//        //Extract Authorization header from the current HTTP request
//        String authHeader = httpServletRequest.getHeader("Authorization");
//
//        if(authHeader!=null){
//            //Add Authorization header to the outgoing request
//            request.getHeaders().add("Authorization",authHeader);
//        }
//        return execution.execute(request,body);
//    }
//}
