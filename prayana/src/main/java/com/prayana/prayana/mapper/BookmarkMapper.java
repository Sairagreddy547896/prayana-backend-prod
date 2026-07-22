package com.prayana.prayana.mapper;

import com.prayana.prayana.dto.BookmarkResponse;
import com.prayana.prayana.entity.Bookmark;
import com.prayana.prayana.entity.PlaceDetails;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

    public BookmarkResponse entityToResponse(Bookmark bookmark){

        BookmarkResponse response=new BookmarkResponse();

        response.setBookmarkId(bookmark.getBookmarkId());
        response.setIsVisited(bookmark.getIsVisited());
        response.setCreatedAt(bookmark.getCreatedAt());
        response.setNote(bookmark.getNote());

        PlaceDetails placeDetails=bookmark.getPlaceDetails();

        response.setPlaceDetailsId(placeDetails.getId());
        response.setGooglePlaceId(placeDetails.getGooglePlaceId());
        response.setDisplayName(placeDetails.getDisplayName());
        response.setFormattedAddress(placeDetails.getFormattedAddress());
        response.setLongitude(placeDetails.getLongitude());
        response.setLatitude(placeDetails.getLatitude());
        response.setNationalPhoneNumber(placeDetails.getNationalPhoneNumber());
        response.setGoogleMapsUri(placeDetails.getGoogleMapsUri());

        return response;
    }
}
