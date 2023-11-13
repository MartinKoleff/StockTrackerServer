package com.koleff.stockserver.stocks.exceptions;

public class JsonNotFoundException extends RuntimeException{
    public JsonNotFoundException(String message){
        super(message);
    }
}
