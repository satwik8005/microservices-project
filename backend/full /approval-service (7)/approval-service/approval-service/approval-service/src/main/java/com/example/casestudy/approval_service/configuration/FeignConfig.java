//package com.example.casestudy.approval_service.configuration;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.apache.logging.log4j.util.Base64Util;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Base64;
//
//@Configuration
//public class FeignConfig {
//
//    @Bean
//    public RequestInterceptor basicAuthRequestInterceptor(){
//        return requestTemplate -> {
//            String username="2784297@tcs.com";
//            String password="ram123";
//            String authHeader="Basic"+ Base64.getEncoder().encodeToString((username+":"+password).getBytes());
//            requestTemplate.header("Authorization",authHeader);
//        };
//    }
//
//
//}
//
