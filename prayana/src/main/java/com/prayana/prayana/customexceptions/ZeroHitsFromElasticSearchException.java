package com.prayana.prayana.customexceptions;

public class ZeroHitsFromElasticSearchException extends RuntimeException {

    public ZeroHitsFromElasticSearchException(String message){
        super(message);
    }

    public ZeroHitsFromElasticSearchException(String message,Throwable cause){
        super(message, cause);
    }

}

