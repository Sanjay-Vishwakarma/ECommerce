package com.sj.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping("/system-check")
    public Map<String, String> systemCheck() {
        // Get the current timestamp
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Create a response map
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "Service is running!");
        response.put("timestamp", formattedDateTime);
        response.put("application", "E-commerce API");
        response.put("description", "System health is good!");

        return response;
    }
}
