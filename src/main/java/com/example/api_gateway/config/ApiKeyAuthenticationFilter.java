package com.example.api_gateway.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiKeyAuthenticationFilter implements GlobalFilter {

    @Value("${api.security.key}")
    private String validApiKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Protect only API endpoints
        if (!path.startsWith("/api/")) {
            return chain.filter(exchange);
        }

        String apiKey = exchange.getRequest()
                .getHeaders()
                .getFirst("X-API-Key");

        if (apiKey == null || !validApiKey.equals(apiKey)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
