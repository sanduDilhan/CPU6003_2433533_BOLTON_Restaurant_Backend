package com.tabletop.restaurant.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @GetMapping("/cors")
    public String testCors() {
        return "CORS is working! Backend is accessible from frontend.";
    }
    
    @GetMapping("/security")
    public String testSecurity() {
        return "Security configuration is working! This endpoint is accessible.";
    }
    
    @PostMapping("/post")
    public String testPost(@RequestBody(required = false) String body) {
        return "POST request successful! Body: " + (body != null ? body : "No body");
    }
    
    @GetMapping("/error-test")
    public String testError() {
        return "No 403 error! Endpoint is accessible.";
    }
}

