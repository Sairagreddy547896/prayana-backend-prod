package com.prayana.prayana.dto;

import com.prayana.prayana.entity.PlaceDetails;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookmarkRequest {


    private Long userId;
    private PlaceDetails placeDetails;
    private Boolean isVisited;
    private String Note;
}
