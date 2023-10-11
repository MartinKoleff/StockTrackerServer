package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class DataNotSavedException extends RuntimeException{
    public DataNotSavedException(String message){
        super(message);
    }
}
