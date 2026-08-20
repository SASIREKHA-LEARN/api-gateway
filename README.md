# API Gateway with Redis Rate Limiting

## Project Description

This project implements an API Gateway using Spring Cloud Gateway.

The gateway routes client requests to a backend service and applies
Redis-based request rate limiting.

MongoDB is used for storing rate-limit configuration data.

## Technologies Used

- Java
- Spring Boot
- Spring Cloud Gateway
- Redis
- MongoDB
- Maven
- JUnit
- Postman

## Architecture

Client
|
v
API Gateway - Port 8080
|
+---- Redis Rate Limiter
|
v
Backend Service - Port 8081

MongoDB
|
+---- Rate Limit Configuration

## Services

### API Gateway

Port: 8080

URL:

http://localhost:8080/api/hello

### Backend Service

Port: 8081

### Redis

Host: localhost

Port: 6379

### MongoDB

Host: localhost

Port: 27017

Database: api_gateway

## Gateway Routing

Requests matching:

/api/**

are routed through the API Gateway to the backend service.

The `/api/` prefix is removed before forwarding the request.

Example:

GET http://localhost:8080/api/hello

The backend receives:

GET http://localhost:8081/hello

## Rate Limiting

Redis-based RequestRateLimiter is configured for the gateway.

Configuration:

- Replenish Rate: 2 requests/second
- Burst Capacity: 2
- Requested Tokens: 1
- Key Resolver: Client IP address

When the rate limit is exceeded, the gateway returns:

HTTP 429 Too Many Requests

## Rate Limit Test

The following test was executed:

GET http://localhost:8080/api/hello

Result:

Request 1 -> HTTP 200
Request 2 -> HTTP 200
Request 3 -> HTTP 429
Request 4 -> HTTP 429
Request 5 -> HTTP 429

This confirms that the Redis rate limiter is working successfully.

## Testing

### JUnit Testing

The project contains tests for:

- Spring application context
- IP KeyResolver
- Missing remote address fallback
- Rate limiter configuration

### Postman Testing

Postman was used to verify:

- Successful API response
- Gateway routing
- Rate-limit behavior
- HTTP 429 response

## Project Structure

src
├── main
│ ├── java
│ │ └── com.example.api_gateway
│ │ ├── config
│ │ │ ├── RateLimiterConfig.java
│ │ │ └── SecurityConfig.java
│ │ ├── controller
│ │ │ └── TestController.java
│ │ ├── model
│ │ │ └── RateLimitConfig.java
│ │ ├── repository
│ │ │ └── RateLimitConfigRepository.java
│ │ └── ApiGatewayApplication.java
│ │
│ └── resources
│ └── application.properties
│
└── test
└── java
└── com.example.api_gateway
├── ApiGatewayApplicationTests.java
└── RateLimiterConfigTest.java

## How to Run

1. Start MongoDB on port 27017.
2. Start Redis on port 6379.
3. Start the backend service on port 8081.
4. Start `ApiGatewayApplication`.
5. The gateway will run on port 8080.
6. Test using:

GET http://localhost:8080/api/hello

Expected response:

Hello from Backend Service!

When the rate limit is exceeded:

HTTP 429 Too Many Requests

## Final Result

The project successfully demonstrates:

- API Gateway routing
- Redis-based rate limiting
- IP-based request identification
- MongoDB integration
- HTTP 429 handling
- JUnit testing
- Postman API testing

## Conclusion

The API Gateway successfully protects the backend service using
Redis-based request rate limiting while providing API routing,
configuration management, automated testing, and API validation