package com.koleff.stockserver.stocks.exceptions;

public class TimezonesNotFoundException extends RuntimeException{
    public TimezonesNotFoundException(String message){
        super(message);
    }
}
