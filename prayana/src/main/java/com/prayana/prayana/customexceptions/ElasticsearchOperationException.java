package com.prayana.prayana.customexceptions;

public class ElasticsearchOperationException extends RuntimeException{

    public ElasticsearchOperationException(String mesage){
        super(mesage);
    }
    public ElasticsearchOperationException(String message, Throwable cause){
        super(message,cause);
    }
}