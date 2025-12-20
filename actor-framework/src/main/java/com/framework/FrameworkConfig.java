package com.framework;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FrameworkConfig {
    
    @Bean
    @LoadBalanced // Indispensable pour Eureka
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}