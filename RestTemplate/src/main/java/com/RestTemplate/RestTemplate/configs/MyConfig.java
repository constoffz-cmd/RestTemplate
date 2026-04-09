package com.RestTemplate.RestTemplate.configs;

import com.RestTemplate.RestTemplate.Services.Communication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Communication communicationService(RestTemplate restTemplate) {
        return new Communication(restTemplate);
    }

}
