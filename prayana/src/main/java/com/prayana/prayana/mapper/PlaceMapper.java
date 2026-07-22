package com.prayana.prayana.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.prayana.prayana.dto.PlaceRequest;
import com.prayana.prayana.dto.PlaceResponse;
import com.prayana.prayana.entity.PlaceDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class PlaceMapper {

    public List<PlaceRequest> handleJsonNode(JsonNode jsonNode){

        if(jsonNode.isArray()){

            return StreamSupport.stream(
                            jsonNode.spliterator(),false)
                    .map(placeNode->convertPlaceJsontoPlaceRequest(placeNode))
                    .toList();
        }

        return List.of(convertPlaceJsontoPlaceRequest(jsonNode));
    }

    public PlaceRequest convertPlaceJsontoPlaceRequest(JsonNode node){

        PlaceRequest placeRequest=new PlaceRequest();

        placeRequest.setGooglePlaceId(node.path("id").asText());
        placeRequest.setNationalPhoneNumber(node.path("nationalPhoneNumber").asText());
        placeRequest.setFormattedAddress(node.path("formattedAddress").asText());
        placeRequest.setLatitude(node.path("location").path("latitude").asDouble());
        placeRequest.setLongitude(node.path("location").path("longitude").asDouble());
        placeRequest.setGoogleMapsUri(node.path("googleMapsUri").asText());
        handleTypes((ArrayNode) node.path("types"),placeRequest);
        placeRequest.setDisplayName(node.path("displayName").path("text").asText());
        placeRequest.setPeriodsJson(node.path("regularOpeningHours").path("periods").toString());
        placeRequest.setWeekdayDescriptionsJson(node.path("regularOpeningHours")
                .path("weekdayDescriptions").toString());
        handleAddressComponent(node.path("addressComponents"),placeRequest);


        return placeRequest;

    }

    private void handleTypes(ArrayNode arrayNode,PlaceRequest placeRequest){

        StringBuilder aliasName=new StringBuilder();

        for(JsonNode type:arrayNode) {
            aliasName.append(type.asText()+",");
        }
        placeRequest.setAliasName(aliasName.toString());
    }


    private void handleAddressComponent(JsonNode addressComponentsNode,
                                        PlaceRequest placeRequest){
        for(JsonNode component:addressComponentsNode) {

            JsonNode typeNode=component.get("types");
            if(typeNode !=null && typeNode.isArray()){
                for(JsonNode type:typeNode){
                    if("locality".equals(type.asText())){
                        placeRequest.setCity(component.get("longText").asText());
                    }
                    if("neighborhood".equals(type.asText())){
                        placeRequest.setArea(component.get("longText").asText());
                    }
                    if("administrative_area_level_2".equals(type.asText())){
                        placeRequest.setDistrict(component.get("longText").asText());
                    }
                    if("administrative_area_level_1".equals(type.asText())){
                        placeRequest.setState(component.get("longText").asText());
                    }
                    if("country".equals(type.asText())){
                        placeRequest.setCountry(component.get("longText").asText());
                    }
                    if("postal_code".equals(type.asText())){
                        placeRequest.setPostalCode(component.get("longText").asText());
                    }
                }
            }
        }
    }

    public PlaceDetails placeRequestToEntity(PlaceRequest placeRequest){


        PlaceDetails placeDetails=new PlaceDetails();

        placeDetails.setGooglePlaceId(placeRequest.getGooglePlaceId());
        placeDetails.setNationalPhoneNumber(placeRequest.getNationalPhoneNumber());
        placeDetails.setFormattedAddress(placeRequest.getFormattedAddress());
        placeDetails.setLatitude(placeRequest.getLatitude());
        placeDetails.setLongitude(placeRequest.getLongitude());
        placeDetails.setGoogleMapsUri(placeRequest.getGoogleMapsUri());
        placeDetails.setDisplayName(placeRequest.getDisplayName());
        placeDetails.setPeriodsJson(placeRequest.getPeriodsJson());
        placeDetails.setAliasName(placeRequest.getAliasName());
        placeDetails.setWeekdayDescriptionsJson(placeRequest.getWeekdayDescriptionsJson());
        placeDetails.setCity(placeRequest.getCity());
        placeDetails.setArea(placeRequest.getArea());
        placeDetails.setDistrict(placeRequest.getDistrict());
        placeDetails.setState(placeRequest.getState());
        placeDetails.setCountry(placeRequest.getCountry());
        placeDetails.setPostalCode(placeRequest.getPostalCode());

        return placeDetails;
    }

    public PlaceResponse entityToPlaceResponse(PlaceDetails placeDetails){

        PlaceResponse placeResponse=new PlaceResponse();
        placeResponse.setId(placeDetails.getId());
        placeResponse.setGooglePlaceId(placeDetails.getGooglePlaceId());
        placeResponse.setNationalPhoneNumber(placeDetails.getNationalPhoneNumber());
        placeResponse.setFormattedAddress(placeDetails.getFormattedAddress());
        placeResponse.setLatitude(placeDetails.getLatitude());
        placeResponse.setLongitude(placeDetails.getLongitude());
        placeResponse.setGoogleMapsUri(placeDetails.getGoogleMapsUri());
        placeResponse.setDisplayName(placeDetails.getDisplayName());
        placeResponse.setPeriodsJson(placeDetails.getPeriodsJson());
        placeResponse.setAliasName(placeDetails.getAliasName());
        placeResponse.setWeekdayDescriptionsJson(placeDetails.getWeekdayDescriptionsJson());
        placeResponse.setCity(placeDetails.getCity());
        placeResponse.setArea(placeDetails.getArea());
        placeResponse.setDistrict(placeDetails.getDistrict());
        placeResponse.setState(placeDetails.getState());
        placeResponse.setCountry(placeDetails.getCountry());
        placeResponse.setPostalCode(placeDetails.getPostalCode());

        return  placeResponse;
    }
}
