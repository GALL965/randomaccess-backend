package com.gael.randomaccess.security;
import org.springframework.http.HttpMethod;
import java.util.List;

import com.gael.randomaccess.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class WebSecurityConfig {

@Bean
public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
            org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
            config.setAllowedOrigins(List.of("*")); // Reemplaza por tu frontend si es necesario
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true); // 🔥 CLAVE para que funcione el preflight
            return config;
        }))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/posts/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/comments").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/comments/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}
