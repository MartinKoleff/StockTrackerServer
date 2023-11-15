package com.koleff.stockserver.stocks.exceptions;

public class StocksNotFoundException extends RuntimeException{
    public StocksNotFoundException(String message){
        super(message);
    }
}
