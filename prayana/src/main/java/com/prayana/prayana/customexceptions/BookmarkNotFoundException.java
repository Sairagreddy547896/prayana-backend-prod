package com.prayana.prayana.customexceptions;

public class BookmarkNotFoundException extends RuntimeException {

    public BookmarkNotFoundException(String message){
        super(message);
    }

    public BookmarkNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}

