package com.prayana.prayana.customexceptions;

public class UserAlreadyExistWithEmailException extends RuntimeException {


    public UserAlreadyExistWithEmailException(String message){
        super(message);
    }

    public UserAlreadyExistWithEmailException(String message,Throwable cause){
        super(message, cause);
    }
}
