package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class StocksNotFoundException extends RuntimeException{
    public StocksNotFoundException(String message){
        super(message);
    }
}
