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
        return http
                .csrf(csrf -> csrf.disable())

                    .cors(cors -> cors.configurationSource(request -> {
    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("*")); // puedes reemplazar * por tu frontend en producción
    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    corsConfig.setAllowedHeaders(List.of("*"));
    return corsConfig;
}))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                       .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
.requestMatchers(HttpMethod.POST, "/api/comments").authenticated()
.requestMatchers(HttpMethod.DELETE, "/api/comments/**").permitAll() 


.requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()

                        .requestMatchers("/api/posts/**").permitAll() 
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}


