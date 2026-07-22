package com.prayana.prayana.customexceptions;

public class BookmarkDeleteException extends RuntimeException {

    public BookmarkDeleteException(String message){
        super(message);
    }

    public BookmarkDeleteException(String message, Throwable cause){
        super(message,cause);
    }
}
