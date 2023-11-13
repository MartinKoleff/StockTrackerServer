package com.koleff.stockserver.stocks.exceptions;

public class DBEmptyException extends RuntimeException{
    public DBEmptyException(String message){
        super(message);
    }
}
