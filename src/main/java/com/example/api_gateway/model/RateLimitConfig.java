package com.example.api_gateway.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "rate_limit_config") public class RateLimitConfig
{
    @Id
    private String id;
    private String clientId;
    private int maxRequests;
    private int windowSeconds;

    public RateLimitConfig() {
    }


    public RateLimitConfig(String clientId, int maxRequests, int windowSeconds) {
        this.clientId = clientId;
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public int getWindowSeconds() {
        return windowSeconds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public void setWindowSeconds(int windowSeconds) {
        this.windowSeconds = windowSeconds;
    }


}