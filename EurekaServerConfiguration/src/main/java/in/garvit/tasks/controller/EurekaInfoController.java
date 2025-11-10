package in.garvit.tasks.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple REST controller for Eureka Server information
 * Provides basic information about the Eureka server
 * 
 * @author garvitpathak27
 */
@RestController
public class EurekaInfoController {

    @Value("${spring.application.name:Eureka Server}")
    private String applicationName;
    
    @Value("${server.port:8085}")
    private String serverPort;

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("applicationName", applicationName);
        info.put("port", serverPort);
        info.put("status", "UP");
        info.put("timestamp", LocalDateTime.now());
        info.put("version", "1.0.0");
        info.put("description", "Eureka Server for Task Management Microservices");
        return info;
    }
    
    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Eureka Server is running");
        response.put("dashboard", "http://localhost:" + serverPort + "/");
        return response;
    }
}