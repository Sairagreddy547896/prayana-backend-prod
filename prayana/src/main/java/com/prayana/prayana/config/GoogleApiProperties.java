package com.prayana.prayana.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "google.api")
public class GoogleApiProperties {

    private String key;
    private String baseUrl;
    private String searchPath;
}
