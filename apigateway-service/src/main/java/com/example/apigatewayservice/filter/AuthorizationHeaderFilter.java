package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders()
                        .containsKey(HttpHeaders.AUTHORIZATION))
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);

            String authorizationHeader = request.getHeaders()
                                                .get(HttpHeaders.AUTHORIZATION)
                                                .get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isValidJwt(jwt))
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);

            return chain.filter(exchange);
        });
    }

    private boolean isValidJwt(String jwt) {
        String subject;
        try {
            subject = Jwts.parser()
                          .setSigningKey(env.getProperty("token.secret"))
                          .parseClaimsJws(jwt)
                          .getBody()
                          .getSubject();
        } catch (Exception e) {
            return false;
        }
        return subject != null && !subject.isEmpty();
    }

    // Mono, Flux -> Spring Webflux 에서 사용하는 단위값?!
    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(errorMessage);
        return response.setComplete();
    }
}
