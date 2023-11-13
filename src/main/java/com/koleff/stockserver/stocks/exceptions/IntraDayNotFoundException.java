package com.koleff.stockserver.stocks.exceptions;

public class IntraDayNotFoundException extends RuntimeException{
    public IntraDayNotFoundException(String message){
        super(message);
    }
}
