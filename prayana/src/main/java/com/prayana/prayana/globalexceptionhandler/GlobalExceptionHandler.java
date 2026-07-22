package com.prayana.prayana.globalexceptionhandler;

import com.prayana.prayana.customexceptions.*;
import com.prayana.prayana.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlacesSaveException.class)
    public ResponseEntity<ErrorResponse> handlePlaceSaveException(
            PlacesSaveException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<ErrorResponse> handleJsonParsingException(
            PlacesSaveException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BookmarkSaveException.class)
    public ResponseEntity<ErrorResponse> handleBookmarkSaveException(
            BookmarkSaveException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlaceNotFoundException(
            PlaceNotFoundException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookmarkNotFoundException(
            BookmarkNotFoundException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookmarkDeleteException.class)
    public ResponseEntity<ErrorResponse> handleBookmakDeleteException(
            BookmarkDeleteException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistWithEmailException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistWithEmailException(
            UserAlreadyExistWithEmailException ex){
        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.CONFLICT);

        return new ResponseEntity<>(err,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserRoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserRoleNotFoundException(
            UserRoleNotFoundException ex){
        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.CONFLICT);

        return new ResponseEntity<>(err,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(
            AuthenticationException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.CONFLICT);

        return new ResponseEntity<>(err,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtException(
            ExpiredJwtException ex){

        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(err,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ElasticsearchConnectionException.class)
    public ResponseEntity<ErrorResponse> elasticsearchConnectionException
            (ElasticsearchConnectionException ex){
        ErrorResponse err=new ErrorResponse();
        err.setMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.SERVICE_UNAVAILABLE);

        return new ResponseEntity<>(err,HttpStatus.UNAUTHORIZED);
    }

}
