package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class StockExchangeNotFoundException extends RuntimeException{
    public StockExchangeNotFoundException(String message){
        super(message);
    }
}
