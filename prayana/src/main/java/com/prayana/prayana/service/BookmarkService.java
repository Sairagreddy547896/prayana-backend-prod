package com.prayana.prayana.service;

import com.prayana.prayana.dto.BookmarkRequest;
import com.prayana.prayana.dto.BookmarkResponse;
import com.prayana.prayana.result.BookmarkResult;

import java.util.List;

public interface BookmarkService {


    BookmarkResult addBookmark(BookmarkRequest bookmarkRequest);

    BookmarkResponse getBookmarkById(Long bookmarkId);

    List<BookmarkResponse> getAllBookmarksforAUserByUserId(Long userId);

    void deleteBookmarkById(Long bookMarkId);

}
