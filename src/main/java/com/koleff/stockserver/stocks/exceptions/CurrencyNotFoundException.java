package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class CurrencyNotFoundException extends RuntimeException{
    public CurrencyNotFoundException(String message){
        super(message);
    }
}
