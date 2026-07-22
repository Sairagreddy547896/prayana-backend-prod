package com.prayana.prayana.customexceptions;

public class ElasticsearchConnectionException extends RuntimeException {

    public ElasticsearchConnectionException(String message){
        super(message);
    }

    public ElasticsearchConnectionException(String message, Throwable cause){
        super(message,cause);
    }

}

