package com.prayana.prayana.service.serviceimpl;

import com.prayana.prayana.customexceptions.*;
import com.prayana.prayana.dto.BookmarkRequest;
import com.prayana.prayana.dto.BookmarkResponse;
import com.prayana.prayana.entity.Bookmark;
import com.prayana.prayana.entity.MyUser;
import com.prayana.prayana.entity.PlaceDetails;
import com.prayana.prayana.mapper.BookmarkMapper;
import com.prayana.prayana.repository.mysql.BookmarkRepo;
import com.prayana.prayana.repository.mysql.MyUserRepo;
import com.prayana.prayana.repository.mysql.PlaceDetailsRepo;
import com.prayana.prayana.result.BookmarkResult;
import com.prayana.prayana.service.BookmarkService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepo bookmarkRepo;
    private final PlaceDetailsRepo placeDetailsRepo;
    private final MyUserRepo userRepo;
    private final BookmarkMapper bookMarkMapper;

    private final Logger log=
            LoggerFactory.getLogger(BookmarkServiceImpl.class);


    @Override
    public BookmarkResult addBookmark(BookmarkRequest bookmarkRequest) {


        Optional<Bookmark> existing = bookmarkRepo.
                findByUserIdAndPlaceDetailsId(bookmarkRequest.getUserId(),
                        bookmarkRequest.getPlaceDetails().getId());

        if (existing.isPresent()) {
            return new BookmarkResult(false,
                    bookMarkMapper.entityToResponse(existing.get()));
        }

        PlaceDetails placeDetails = placeDetailsRepo.findById(
                        bookmarkRequest.getPlaceDetails().getId())
                .orElseThrow(
                        () -> new PlaceNotFoundException("not able to find " +
                                "the placeDetails with given id "
                                + bookmarkRequest.getPlaceDetails().getId())
                );

        MyUser user = userRepo.findById(bookmarkRequest.getUserId())
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "user not found with given id "
                                        + bookmarkRequest.getUserId()
                        ));


        Bookmark bookmark = new Bookmark();

        bookmark.setUser(user);
        bookmark.setPlaceDetails(placeDetails);
        bookmark.setIsVisited(false);

        Bookmark savedBookmark;
        try {
            savedBookmark = bookmarkRepo.save(bookmark);
        } catch (Exception ex) {
            log.error("failed to add bookmark", ex);
            throw new BookmarkSaveException("unable to add bookmark", ex);
        }


        return new BookmarkResult(true,
                bookMarkMapper.entityToResponse(savedBookmark));
    }

    @Override
    public BookmarkResponse getBookmarkById(Long bookmarkId) {

        Bookmark bookmark    =  bookmarkRepo.findById(bookmarkId)
                .orElseThrow(
                        ()-> new BookmarkNotFoundException(
                                "bookmark not found with given Id "+bookmarkId)
                );

        return bookMarkMapper.entityToResponse(bookmark);

    }

    private Bookmark getBookmarkEntityById(Long bookmarkId){
        Bookmark bookmark    =  bookmarkRepo.findById(bookmarkId)
                .orElseThrow(
                        ()-> new BookmarkNotFoundException(
                                "bookmark not found with given Id "+bookmarkId)
                );

        return bookmark;
    }

    @Override
    public List<BookmarkResponse> getAllBookmarksforAUserByUserId(Long userId) {

        List<Bookmark> bookmarksList=bookmarkRepo
                .getAllBookmarksByUserId(userId);

        if(bookmarksList.isEmpty()){
            return new ArrayList<>();
        }

        return  bookmarksList.stream()
                .map(
                        bookmark -> bookMarkMapper.entityToResponse(bookmark)
                ).toList();

    }

    @Override
    public void deleteBookmarkById(Long bookMarkId) {

        Bookmark bookmark  =   getBookmarkEntityById(bookMarkId);

        try {
            bookmarkRepo.delete(bookmark);
        } catch (Exception e) {
            throw new BookmarkDeleteException("bookmark cannot be deleted",e);
        }
    }

}
