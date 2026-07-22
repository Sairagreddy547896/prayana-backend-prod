package com.prayana.prayana.repository.mysql;

import com.prayana.prayana.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepo extends JpaRepository<Bookmark,Long> {

    @Query("from Bookmark where user.id=:userId")
    List<Bookmark> getAllBookmarksByUserId(@Param("userId") Long userId);

    Optional<Bookmark> findByUserIdAndPlaceDetailsId(Long id,Long placeId);

    @Query(value = """
SELECT pd.*,
(
6371 * acos(
cos(radians(:lat)) *
cos(radians(pd.latitude)) *
cos(radians(pd.longitude) - radians(:lng)) +
sin(radians(:lat)) *
sin(radians(pd.latitude))
)
) AS distance
FROM bookmarks_places b
JOIN place_details pd ON b.place_id = pd.id
WHERE b.user_id = :userId
HAVING distance <= :radius
ORDER BY distance
""", nativeQuery = true)
    List<Object[]> findNearbyBookmarks(Long userId, Double lat, Double lng, Double radius);

}
