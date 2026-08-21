package com.example.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().getPath();

        String clientIp = exchange.getRequest()
                .getHeaders()
                .getFirst("X-Forwarded-For");

        if (clientIp == null && exchange.getRequest().getRemoteAddress() != null) {
            clientIp = exchange.getRequest()
                    .getRemoteAddress()
                    .getAddress()
                    .getHostAddress();
        }

        final String finalClientIp = clientIp;

        return chain.filter(exchange)
                .doFinally(signal -> {

                    long duration =
                            System.currentTimeMillis() - startTime;

                    HttpStatusCode status =
                            exchange.getResponse().getStatusCode();

                    int statusCode =
                            status != null ? status.value() : 0;

                    if (statusCode == 429) {
                        logger.warn(
                                "RATE_LIMIT_EXCEEDED | method={} | path={} | clientIp={} | status={} | durationMs={}",
                                method,
                                path,
                                finalClientIp,
                                statusCode,
                                duration
                        );
                    } else {
                        logger.info(
                                "API_REQUEST | method={} | path={} | clientIp={} | status={} | durationMs={}",
                                method,
                                path,
                                finalClientIp,
                                statusCode,
                                duration
                        );
                    }
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}