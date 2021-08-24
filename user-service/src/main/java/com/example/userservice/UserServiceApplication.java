package com.example.userservice;

import feign.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    private static ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                   .setMatchingStrategy(STRICT);
        return this.modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * feign client 사용할 때, 로그 찍기 위해서는 빈 등록을 해줘야 함!
     */
    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }

    /** errorDecoder 를 이용한 error handling */
    //    @Bean
    //    public FeignErrorDecoder feignErrorDecoder() {
    //        return new FeignErrorDecoder();
    //    }
}

/**
 * terminal 로 실행하기
 * ./gradlew bootRun --args='--server.port=9003'
 */