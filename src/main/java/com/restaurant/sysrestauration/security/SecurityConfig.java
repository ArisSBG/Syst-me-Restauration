package com.restaurant.sysrestauration.security;

import com.restaurant.sysrestauration.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetailsService);
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF pour éviter les blocages sur H2 Console
                .csrf(csrf -> csrf.disable())
                // Configure les headers pour permettre l'affichage de H2 Console
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                // Configure la gestion de session en mode stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure les règles d'autorisation
                .authorizeHttpRequests(authz -> authz
                        // Autorise l'inscription et la connexion
                        .requestMatchers(HttpMethod.POST, "/login", "/customers").permitAll()
                        // Autorise l'accès à Swagger
                        .requestMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Autorise l'accès à la console H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // Endpoints accessibles aux clients
                        .requestMatchers(HttpMethod.GET, "/menus", "/menus/**", "/dishes", "/dishes/**")
                        .hasAnyAuthority("ROLE_CLIENT", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/orders")
                        .hasAnyAuthority("ROLE_CLIENT", "ROLE_ADMIN")
                        // Les autres endpoints nécessitent le rôle ADMIN
                        .anyRequest().permitAll()

                )
                // Ajoute le filtre JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
