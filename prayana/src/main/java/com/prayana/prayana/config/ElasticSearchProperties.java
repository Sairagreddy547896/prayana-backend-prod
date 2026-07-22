package com.prayana.prayana.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticSearchProperties {

    private String host;
    private int port;
    private String scheme;
}
