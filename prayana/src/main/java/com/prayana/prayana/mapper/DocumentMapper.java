package com.prayana.prayana.mapper;

import com.prayana.prayana.document.Location;
import com.prayana.prayana.document.PlaceDocument;
import com.prayana.prayana.entity.PlaceDetails;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public PlaceDocument entityToDocument(PlaceDetails placeDetails){
        PlaceDocument placeDocument=new PlaceDocument();

        placeDocument.setId(placeDetails.getGooglePlaceId());
        placeDocument.setName(placeDetails.getDisplayName());
        placeDocument.setArea(placeDetails.getArea());
        placeDocument.setCity(placeDetails.getCity());
        placeDocument.setDistrict(placeDetails.getDistrict());
        placeDocument.setState(placeDetails.getState());
        placeDocument.setAliasName(placeDetails.getAliasName());
        placeDocument.setCountry(placeDetails.getCountry());
        placeDocument.setPostalCode(placeDetails.getPostalCode());
        placeDocument.setLocation(new Location(placeDetails.getLatitude(),
                placeDetails.getLongitude()));
        return placeDocument;
    }
}
