package com.koleff.stockserver.stocks.exceptions;

public class CurrenciesNotFoundException extends RuntimeException{
    public CurrenciesNotFoundException(String message){
        super(message);
    }
}
