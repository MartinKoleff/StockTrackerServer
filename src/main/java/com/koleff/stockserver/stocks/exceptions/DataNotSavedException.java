package com.koleff.stockserver.stocks.exceptions;

public class DataNotSavedException extends RuntimeException{
    public DataNotSavedException(String message){
        super(message);
    }
}
