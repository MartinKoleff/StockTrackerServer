package com.koleff.stockserver.stocks.exceptions;

public class TimezoneNotFoundException extends RuntimeException{
    public TimezoneNotFoundException(String message){
        super(message);
    }
}
