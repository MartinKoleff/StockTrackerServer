package com.koleff.stockserver.stocks.exceptions;

public class StockExchangeNotFoundException extends RuntimeException{
    public StockExchangeNotFoundException(String message){
        super(message);
    }
}
