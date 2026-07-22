package com.prayana.prayana.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookmarkResponse {

    private Long bookmarkId;
    private LocalDateTime createdAt;
    private Boolean isVisited;
    private String Note;

    private Long placeDetailsId;
    private String googlePlaceId;
    private String displayName;
    private String formattedAddress;
    private Double latitude;
    private Double longitude;
    private String nationalPhoneNumber;
    private String googleMapsUri;
}
