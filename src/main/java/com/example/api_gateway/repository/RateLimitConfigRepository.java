package com.example.api_gateway.repository;

import com.example.api_gateway.model.RateLimitConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RateLimitConfigRepository
        extends MongoRepository<RateLimitConfig, String> {

    Optional<RateLimitConfig> findByClientId(String clientId);
}