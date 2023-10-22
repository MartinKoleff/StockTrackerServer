package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class TimezonesNotFoundException extends RuntimeException{
    public TimezonesNotFoundException(String message){
        super(message);
    }
}
