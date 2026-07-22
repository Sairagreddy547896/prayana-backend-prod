package com.prayana.prayana.customexceptions;

public class PlaceNotFoundException extends RuntimeException {

    public PlaceNotFoundException(String message){
        super(message);
    }

    public PlaceNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}
