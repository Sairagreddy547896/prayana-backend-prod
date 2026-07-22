package com.prayana.prayana.controller;


import com.prayana.prayana.dto.BookmarkRequest;
import com.prayana.prayana.dto.BookmarkResponse;
import com.prayana.prayana.result.BookmarkResult;
import com.prayana.prayana.service.BookmarkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmark")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @GetMapping("/get-bookmark-by-id/{bookmarkId}")
    public ResponseEntity<BookmarkResponse> getBookmarkById(@PathVariable("bookmarkId")
                                                            Long bookmarkId){

        return new ResponseEntity<>(bookmarkService.getBookmarkById(bookmarkId),
                HttpStatus.FOUND);
    }
    @PostMapping("/add-bookmark")
    public ResponseEntity<BookmarkResponse> addBookmark(
            @RequestBody BookmarkRequest bookmarkRequest){
        BookmarkResult response=bookmarkService.addBookmark(bookmarkRequest);
        if(response.created()){
            return new ResponseEntity<>(response.response(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response.response(), HttpStatus.OK);
    }

    @GetMapping("/all-bookmarks-by-userid/{userId}")
    public ResponseEntity<List<BookmarkResponse>> getAllBookmarksforAUserByUserId(
            @PathVariable("userId") long userId
    ){
        return new ResponseEntity<>(bookmarkService.getAllBookmarksforAUserByUserId(userId),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete-bookmark-by-id")
    public ResponseEntity<String> deleteBookmarkById(@RequestParam("bookmarkId")
                                                     long bookmarkId){
        return new ResponseEntity("bookmark deleted",HttpStatus.OK);
    }
}
