package com.prayana.prayana.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlaceRequest {

    private String googlePlaceId;

    private String displayName;

    private String formattedAddress;

    private Double latitude;

    private Double longitude;

    private String nationalPhoneNumber;

    private String googleMapsUri;

    private String weekdayDescriptionsJson;

    private String periodsJson;

    private Boolean openNow;

    private LocalDateTime lastSyncedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String aliasName;

    private String city;

    //locality
    private String area;

    //administrative_level_2
    private String district;

    //administrative_level_1
    private String state;

    private String country;

    private String postalCode;
}
