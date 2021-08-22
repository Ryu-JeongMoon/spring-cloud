package com.example.userservice;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    private static ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        if (modelMapper != null) {
            modelMapper.getConfiguration()
                       .setMatchingStrategy(STRICT);
            return modelMapper;
        }

        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                   .setMatchingStrategy(STRICT);
        return this.modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

/**
 * terminal 로 실행하기
 * ./gradlew bootRun --args='--server.port=9003'
 */