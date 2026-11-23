package com.prayana.prayana.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prayana.prayana.config.GoogleApiProperties;
import com.prayana.prayana.constants.GoogleApiConstants;
import com.prayana.prayana.customexceptions.JsonParsingException;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class GooglePlacesClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final GoogleApiProperties properties;


    public JsonNode searchPlaces(String query)  {

        String url =
                   properties.getBaseUrl()+ properties.getSearchPath();
        HttpHeaders headers=new org.springframework.http.HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(GoogleApiConstants.HEADER_API_KEY,
                properties.getKey());
        headers.set(GoogleApiConstants.HEADER_FIELD_MASK,
                GoogleApiConstants.SEARCH_FIELD_MASK);

        String body= """
                {
                  "textQuery":"%s"
                }
                """.formatted(query.replace("\"","\\\""));

        HttpEntity<String> request  =
                new HttpEntity<>(body,headers);

        ResponseEntity<String> response   =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        String.class
                );
        try {
            return objectMapper.readTree(response.getBody());
        }
        catch (JsonProcessingException ex){
            throw new JsonParsingException(
                    "Failed to parse external API response from Google Place API",
                    ex.getCause());
        }
    }
}
