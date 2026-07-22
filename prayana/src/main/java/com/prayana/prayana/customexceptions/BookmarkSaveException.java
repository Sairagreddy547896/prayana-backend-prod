package com.prayana.prayana.customexceptions;

public class BookmarkSaveException extends  RuntimeException{

    public BookmarkSaveException(String message){
        super(message);
    }
    public BookmarkSaveException(String message, Throwable cause){
        super(message,cause);
    }

}
