package com.example.fileStorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // бүх URL-үүдийг хамрах
                .allowedOrigins("http://localhost:3000") // зөвшөөрөгдсөн origin (Frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // зөвшөөрөгдсөн HTTP method-үүд
                .allowedHeaders("*") // бүх header-ийг зөвшөөрөх
                .allowCredentials(true); // credentials (жишээ нь, cookies) зөвшөөрөх
    }
}
