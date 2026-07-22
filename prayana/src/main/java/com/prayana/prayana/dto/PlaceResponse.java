package com.prayana.prayana.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlaceResponse {

    private Long id;
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

    private String aliasName;
    private String city;
    private String area;

    //administrative_level_2
    private String district;

    //administrative_level_1
    private String state;

    private String country;

    private String postalCode;
}
