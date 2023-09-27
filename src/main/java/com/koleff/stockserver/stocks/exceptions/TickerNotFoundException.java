package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class TickerNotFoundException extends RuntimeException{
    public TickerNotFoundException(String message){
        super(message);
    }
}
