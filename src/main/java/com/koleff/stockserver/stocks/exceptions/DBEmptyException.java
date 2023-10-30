package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class DBEmptyException extends RuntimeException{
    public DBEmptyException(String message){
        super(message);
    }
}