package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class TimezoneNotFoundException extends RuntimeException{
    public TimezoneNotFoundException(String message){
        super(message);
    }
}
