package com.prayana.prayana.customexceptions;

public class UserRoleNotFoundException extends RuntimeException{

    public UserRoleNotFoundException(String message){

        super(message);
    }

    public UserRoleNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}
