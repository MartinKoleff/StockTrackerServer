package com.koleff.stockserver.stocks.exceptions;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //Needed if not using the ErrorHandler
public class JsonNotFoundException extends RuntimeException{
    public JsonNotFoundException(String message){
        super(message);
    }
}
