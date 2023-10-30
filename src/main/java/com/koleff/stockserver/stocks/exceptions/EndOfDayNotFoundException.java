package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class EndOfDayNotFoundException extends RuntimeException{
    public EndOfDayNotFoundException(String message){
        super(message);
    }
}
