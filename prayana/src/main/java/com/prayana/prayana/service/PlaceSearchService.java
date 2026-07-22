package com.prayana.prayana.service;


import com.prayana.prayana.dto.PlaceResponse;
import com.prayana.prayana.entity.PlaceDetails;

import java.util.List;

public interface PlaceSearchService {

    List<PlaceResponse> handleSearchPlaces(String searchQuery);

    PlaceDetails savePlace(PlaceDetails placeDetails);

    void savePlaces(List<PlaceDetails> placeDetailsList);

    //to get placeDetails by googlePlaceID
    PlaceDetails getPlaceByGoogleId(String googlePlaceId);

}
