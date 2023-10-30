package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class CurrenciesNotFoundException extends RuntimeException{
    public CurrenciesNotFoundException(String message){
        super(message);
    }
}
