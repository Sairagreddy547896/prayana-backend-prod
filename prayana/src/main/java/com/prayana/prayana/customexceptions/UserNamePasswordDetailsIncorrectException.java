package com.prayana.prayana.customexceptions;

public class UserNamePasswordDetailsIncorrectException extends RuntimeException {

    public UserNamePasswordDetailsIncorrectException(String message){
        super(message);
    }

    public UserNamePasswordDetailsIncorrectException(
            String message,Throwable cause){
        super(message,cause);
    }

}