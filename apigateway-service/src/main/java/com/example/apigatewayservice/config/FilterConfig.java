package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

    //@Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                      .route(r -> r.path("/first-service/**")
                                   .filters(f -> f.addRequestHeader("first-request", "request-header-1")
                                                  .addResponseHeader("first-response", "response-header-1"))
                                   .uri("http://localhost:8081"))
                      .route(r -> r.path("/second-service/**")
                                   .filters(f -> f.addRequestHeader("second-request", "request-header-2")
                                                  .addResponseHeader("second-response", "response-header-2"))
                                   .uri("http://localhost:8082"))
                      .build();
    }
}
