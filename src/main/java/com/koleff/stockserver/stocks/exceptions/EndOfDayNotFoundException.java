package com.koleff.stockserver.stocks.exceptions;

public class EndOfDayNotFoundException extends RuntimeException{
    public EndOfDayNotFoundException(String message){
        super(message);
    }
}
