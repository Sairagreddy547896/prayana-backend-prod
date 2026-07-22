package com.prayana.prayana.customexceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}

