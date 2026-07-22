package com.prayana.prayana.repository.mysql;

import com.prayana.prayana.entity.PlaceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceDetailsRepo extends JpaRepository<PlaceDetails,Long> {

    Optional<PlaceDetails> findByGooglePlaceId(String googlePlaceId);
}
