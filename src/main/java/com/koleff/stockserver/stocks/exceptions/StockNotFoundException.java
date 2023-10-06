package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message){
        super(message);
    }
}
