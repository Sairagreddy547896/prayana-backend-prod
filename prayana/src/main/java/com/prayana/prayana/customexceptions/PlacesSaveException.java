package com.prayana.prayana.customexceptions;

public class PlacesSaveException extends RuntimeException{

    public PlacesSaveException(String message){
        super(message);
    }

    public PlacesSaveException(String message, Throwable cause){
        super(message,cause);
    }
}
