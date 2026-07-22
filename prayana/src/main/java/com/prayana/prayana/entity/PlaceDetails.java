package com.prayana.prayana.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "place_details")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlaceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // External provider id
    @Column(name = "google_place_id", unique = true, nullable = false)
    private String googlePlaceId;

    private String displayName;

    @Column(length = 1000)
    private String formattedAddress;

    private Double latitude;

    private Double longitude;

    private String nationalPhoneNumber;

    @Column(length = 2000)
    private String googleMapsUri;

    // Human-readable weekly timings
    @Column(columnDefinition = "TEXT")
    private String weekdayDescriptionsJson;

    // Structured periods JSON for future logic
    @Column(columnDefinition = "TEXT")
    private String periodsJson;

    private Boolean openNow;

    private LocalDateTime lastSyncedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //like temple or historic place or adventure
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


    @OneToMany(mappedBy = "placeDetails",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Bookmark> bookmarks=new HashSet<>();


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastSyncedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}