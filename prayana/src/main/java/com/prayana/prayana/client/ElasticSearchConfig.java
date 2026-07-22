package com.prayana.prayana.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import com.prayana.prayana.config.ElasticSearchProperties;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AllArgsConstructor
public class ElasticSearchConfig  {

    private final ElasticSearchProperties properties;


    @Bean(destroyMethod = "close")
    public Rest5Client rest5Client(){

//        BasicCredentialsProvider credentialsProvider=new BasicCredentialsProvider();
//
//         credentialsProvider.setCredentials(
//                 new AuthScope(properties.getHost(), properties.getPort()),
//                         new UsernamePasswordCredentials(properties.getUsername(),
//                                 properties.getPassword().toCharArray())
//         );

        return   Rest5Client.builder(
                        new HttpHost(properties.getScheme(),properties.getHost(),properties.getPort()
                        ))
//                 .setHttpClientConfigCallback(
//                         httpAsyncClientBuilder ->
//                                 httpAsyncClientBuilder
//                                         .setDefaultCredentialsProvider(credentialsProvider))
                .build();

    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(Rest5Client rest5Client){
        return new Rest5ClientTransport(rest5Client,new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport){
        return new ElasticsearchClient(elasticsearchTransport);
    }


}
