package com.prayana.prayana.service.serviceimpl;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.databind.JsonNode;
import com.prayana.prayana.client.GooglePlacesClient;
import com.prayana.prayana.customexceptions.PlaceNotFoundException;
import com.prayana.prayana.customexceptions.PlacesSaveException;
import com.prayana.prayana.customexceptions.ZeroHitsFromElasticSearchException;
import com.prayana.prayana.document.PlaceDocument;
import com.prayana.prayana.dto.PlaceRequest;
import com.prayana.prayana.dto.PlaceResponse;
import com.prayana.prayana.entity.PlaceDetails;
import com.prayana.prayana.mapper.DocumentMapper;
import com.prayana.prayana.mapper.PlaceMapper;
import com.prayana.prayana.repository.elasticsearch.PlaceSearchElasticRepository;
import com.prayana.prayana.repository.mysql.PlaceDetailsRepo;
import com.prayana.prayana.service.PlaceSearchService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlaceSearchServiceImpl implements PlaceSearchService {

    private final GooglePlacesClient googlePlacesClient;
    private final PlaceDetailsRepo placeDetailsRepo;
    private final PlaceSearchElasticRepository placeSearchElasticRepository;
    private final PlaceMapper placeMapper;
    private final DocumentMapper documentMapper;

    private static final Logger log=
            LoggerFactory.getLogger(PlaceSearchServiceImpl.class);

    @Transactional
    public void savePlaces(List<PlaceDetails> placeDetailsList){

        if(placeDetailsList.isEmpty()|| placeDetailsList==null){
            log.error("placeDetails list is empty or null");
            return;
        }
        try{
            placeDetailsRepo.saveAll(placeDetailsList);
        }
        catch (Exception ex){
            log.error("Failed to save places",ex);
            throw new PlacesSaveException("unable to save place details", ex);
        }

    }

    @Transactional
    public PlaceDetails savePlace(PlaceDetails placeDetails){

        try{
            return placeDetailsRepo.save(placeDetails);
        }
        catch (Exception ex){
            log.error("Failed to save Plaves",ex);
            throw new PlacesSaveException("unable to save place details",ex);
        }
    }

    public List<PlaceResponse>  handleSearchPlaces(String searchQuery){

        return   searchPlacesFromElastic(searchQuery).stream().map(
                placeDetails -> placeMapper.entityToPlaceResponse(placeDetails)
        ).toList();

    }

    private List<PlaceDetails> searchPlacesFromElastic(String searchQuery){


        SearchResponse<PlaceDocument> response = placeSearchElasticRepository
                .searchPlaces(searchQuery);

        if (response.hits().total().value() == 0) {
            //return searchPlacesFromGooglePlacesAPI(searchQuery);
            throw new ZeroHitsFromElasticSearchException
                    ("no hits from elastic search for " + searchQuery);
        }

        List<PlaceDetails> placeDetails = new ArrayList<>();

        for (Hit<PlaceDocument> hit : response.hits().hits()) {
            placeDetails.add(getPlaceByGoogleId(hit.id()));
        }
        return placeDetails;
    }

    private List<PlaceDetails> searchPlacesFromGooglePlacesAPI(String searchQuery) {

        JsonNode  jsonNode = googlePlacesClient.searchPlaces(searchQuery);

        List<PlaceRequest> placeRequests= placeMapper.handleJsonNode(jsonNode.path("places"));

        List<PlaceDetails> placeDetailsList = new ArrayList<>();

        for(PlaceRequest place:placeRequests){
            PlaceDetails placeDetails=    placeDetailsRepo
                    .findByGooglePlaceId(place.getGooglePlaceId())
                    .orElseGet(()->
                            savePlace(placeMapper.placeRequestToEntity(place))
                    );
            placeDetailsList.add(placeDetails);
            placeSearchElasticRepository.savePlaces(documentMapper.entityToDocument(placeDetails));
        }

        return  placeDetailsList;

    }

    public PlaceDetails getPlaceByGoogleId(String googlePlaceId) {

        PlaceDetails placeDetails = placeDetailsRepo.
                findByGooglePlaceId(googlePlaceId).orElseThrow(
                        ()  -> new PlaceNotFoundException("place not found with given id "
                                +googlePlaceId));

        return placeDetails;
    }
}
