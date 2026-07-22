package com.prayana.prayana.customexceptions;

public class UserRegistrationException extends RuntimeException{

    public UserRegistrationException(String message){
        super(message);
    }
    public UserRegistrationException(String message, Throwable cause){
        super(message,cause);
    }
}
