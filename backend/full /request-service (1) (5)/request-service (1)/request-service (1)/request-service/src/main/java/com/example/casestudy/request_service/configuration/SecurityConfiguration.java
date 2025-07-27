//package com.example.casestudy.request_service.configuration;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cglib.proxy.NoOp;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//
////    @Autowired
////    private AuthenticationManagerBuilder authenticationManagerBuilder;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }
//
////    @Autowired
////    public void configure(AuthenticationManagerBuilder auth) throws Exception {
////
////        auth.
////                userDetailsService(userDetailsService);
////
////    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authz -> authz
//
//                        .requestMatchers("/", "/index.html", "/employee/register").permitAll()
//
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui/index.html").permitAll()
//
//                        .requestMatchers("/api/employee/find-by-username").permitAll()
//
//                        .requestMatchers("/api/seat-request/raiseRequestForDeveloper").hasRole("DEVELOPER")
//
//                        .requestMatchers("/api/seat-request/raiseMultipleSeatRequest").hasRole("MANAGER")
//
//                        .requestMatchers("/api/seat-request/allRequestByDeveloper").hasRole("MANAGER")
//
//                        .requestMatchers("/api/seat-request/getSeatForManager").hasRole("MANAGER")
//
//                        .requestMatchers("/api/seat-request/update-status/{id}").hasRole("MANAGER")
//
//                        .requestMatchers("/api/seat-request//update-status-for-mass-seat/{id}").hasRole("ADMIN")
//
//                        .requestMatchers("/api/seat-request/allRequestByManager").hasRole("ADMIN")
//
//
//
//                        // All other requests should be authenticated
//                        .anyRequest().authenticated()
//                );
//
//                http.formLogin(Customizer.withDefaults());
//
//                http.httpBasic(Customizer.withDefaults());
//                http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                http.logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//
//
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return NoOpPasswordEncoder.getInstance();
////    }
//
////    @Bean
////    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
////        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
////    }
//
//
//
//}
