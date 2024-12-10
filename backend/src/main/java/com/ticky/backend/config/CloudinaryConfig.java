package com.ticky.backend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dmjpdjyes");
        config.put("api_key", "396659396316768");
        config.put("api_secret", "25eXL93stzL3tcsF0XKm-LoTks0");
        return new Cloudinary(config);
    }
}
