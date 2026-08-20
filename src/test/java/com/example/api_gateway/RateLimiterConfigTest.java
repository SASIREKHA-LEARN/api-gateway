package com.example.api_gateway;

import com.example.api_gateway.config.RateLimiterConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RateLimiterConfigTest {

    @Test
    void ipKeyResolverShouldReturnClientIp() {

        // Mock the request
        ServerHttpRequest request = mock(ServerHttpRequest.class);

        // Mock the exchange
        ServerWebExchange exchange = mock(ServerWebExchange.class);

        // Simulate client IP
        InetSocketAddress clientAddress =
                new InetSocketAddress("192.168.1.100", 12345);

        when(exchange.getRequest()).thenReturn(request);
        when(request.getRemoteAddress()).thenReturn(clientAddress);

        // Create configuration
        RateLimiterConfig config = new RateLimiterConfig();

        // Resolve IP
        String resolvedIp =
                config.ipKeyResolver()
                        .resolve(exchange)
                        .block();

        // Verify
        assertEquals("192.168.1.100", resolvedIp);
    }

    @Test
    void ipKeyResolverShouldReturnUnknownWhenRemoteAddressIsNull() {

        // Mock the request
        ServerHttpRequest request = mock(ServerHttpRequest.class);

        // Mock the exchange
        ServerWebExchange exchange = mock(ServerWebExchange.class);

        // Simulate missing remote address
        when(exchange.getRequest()).thenReturn(request);
        when(request.getRemoteAddress()).thenReturn(null);

        // Create configuration
        RateLimiterConfig config = new RateLimiterConfig();

        // Resolve IP
        String resolvedIp =
                config.ipKeyResolver()
                        .resolve(exchange)
                        .block();

        // Verify fallback
        assertEquals("unknown", resolvedIp);
    }
}