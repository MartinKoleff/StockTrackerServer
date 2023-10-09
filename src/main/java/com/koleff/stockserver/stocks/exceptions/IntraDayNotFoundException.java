package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class IntraDayNotFoundException extends RuntimeException{
    public IntraDayNotFoundException(String message){
        super(message);
    }
}
