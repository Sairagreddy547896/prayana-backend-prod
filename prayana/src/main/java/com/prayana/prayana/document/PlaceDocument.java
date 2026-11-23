package com.prayana.prayana.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDocument {

    private String id;
    private String name;
    private String address;
    private String aliasName;
    private String area;
    private String city;
    private String district;
    private String state;
    private String country;
    private String postalCode;
    private Location location;
}
