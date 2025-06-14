package com.gael.randomaccess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
public class RandomaccessBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandomaccessBackendApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
public PasswordEncoder passwordEncoder() {
    return new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString(); // sin codificar
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword); // comparación directa
        }
    };
}

}
