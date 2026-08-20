package com.example.api_gateway.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "API Gateway is running successfully!";
    }

    @GetMapping("/test")
    public String test() {
        return "Test API response";
    }
}