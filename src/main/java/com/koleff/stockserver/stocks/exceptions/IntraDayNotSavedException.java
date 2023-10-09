package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class IntraDayNotSavedException extends RuntimeException{
    public IntraDayNotSavedException(String message){
        super(message);
    }
}
