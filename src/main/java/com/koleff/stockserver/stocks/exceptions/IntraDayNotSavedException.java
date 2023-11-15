package com.koleff.stockserver.stocks.exceptions;

public class IntraDayNotSavedException extends RuntimeException{
    public IntraDayNotSavedException(String message){
        super(message);
    }
}
