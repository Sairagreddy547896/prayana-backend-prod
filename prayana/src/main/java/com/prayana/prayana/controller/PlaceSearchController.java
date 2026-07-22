package com.prayana.prayana.controller;


import com.prayana.prayana.dto.PlaceResponse;
import com.prayana.prayana.service.PlaceSearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@AllArgsConstructor
public class PlaceSearchController {


    private final PlaceSearchService placeSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<PlaceResponse>> searchPlaces(@RequestParam("searchString") String searchString){

        System.out.println(" Search String ------------"+searchString);
        return new ResponseEntity<>(placeSearchService.
                handleSearchPlaces(searchString),
                HttpStatus.OK);

    }

    @GetMapping("/{placeId}")
    public ResponseEntity<?> getPlaceById(@PathVariable("placeId") String placeId){
        System.out.print(placeId);
        return null;
    }

//    @GetMapping("/google/{googleMapId}")
//    public ResponseEntity<?> getPlaceByGoogleId(@PathVariable("googleMapId") String googleMapId){
//
//           placeSearchService.getPlaceByGoogleId(googleMapId);
//
//    }
}
