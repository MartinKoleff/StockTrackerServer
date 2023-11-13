package com.koleff.stockserver.stocks.exceptions;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message){
        super(message);
    }
}
