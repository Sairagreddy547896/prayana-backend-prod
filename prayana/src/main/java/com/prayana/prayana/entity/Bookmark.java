package com.prayana.prayana.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Table(name = "bookmark_places")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private MyUser user;

    @ManyToOne
    @JoinColumn(name="place_id")
    private PlaceDetails placeDetails;

    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;

    private Boolean isVisited;

    private String Note;

    @PrePersist
    public void prePersiste(){
        createdAt= LocalDateTime.now();
    }
}
