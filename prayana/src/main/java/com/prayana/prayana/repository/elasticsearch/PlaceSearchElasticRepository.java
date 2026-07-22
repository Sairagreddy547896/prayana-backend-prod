package com.prayana.prayana.repository.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.prayana.prayana.customexceptions.ElasticsearchConnectionException;
import com.prayana.prayana.document.PlaceDocument;
import com.prayana.prayana.util.ElasticIndexes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.ConnectException;

@Repository
@AllArgsConstructor
public class PlaceSearchElasticRepository {

    private final ElasticsearchClient client;


    public void savePlaces(PlaceDocument placeDocument){

        try{
            IndexResponse response= client.index(i -> i
                    .index(ElasticIndexes.PLACES)
                    .id(placeDocument.getId())
                    .document(placeDocument));
            System.out.print(response.result());

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public SearchResponse<PlaceDocument> searchPlaces(String searchQuery) {


        try {
            SearchResponse<PlaceDocument>    response =
                    client.search(s -> s
                                    .index(ElasticIndexes.PLACES)
                                    .query(q -> q
                                            .match(t -> t
                                                    .field("name")
                                                    .query(searchQuery))),
                            PlaceDocument.class);
            return response;
        } catch (ConnectException e) {
            throw new ElasticsearchConnectionException(
                    "Unable to connect to elastic search",e);
        } catch (IOException e) {
            throw new ElasticsearchConnectionException(
                    "Error occurred while retrieving data from Elasticsearch",e);
        }


    }
}
