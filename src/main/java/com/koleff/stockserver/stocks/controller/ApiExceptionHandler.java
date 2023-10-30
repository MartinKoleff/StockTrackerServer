package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.exceptions.ApiException;
import com.koleff.stockserver.stocks.exceptions.ApiRequestException;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    private final static Logger logger = LogManager.getLogger(ApiExceptionHandler.class);
    @ExceptionHandler(value = ApiRequestException.class) //To make unified error handler. All ApiRequestExceptions to have the payload data inside them (integrate ApiException inside ApiRequestException).
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){

        //Returned to the client
        ApiException payload = new ApiException(
                e.getMessage(),
                e.getCause(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );

        logger.error("Error thrown with status code: " + payload.getHttpStatusCode());

        return new ResponseEntity(
                payload,
                payload.getHttpStatus()
        );
    }

    @ExceptionHandler(value = StockNotFoundException.class)
    public ResponseEntity<Object> handleStockNotFoundException(StockNotFoundException e){

        //Returned to the client
        ApiException payload = new ApiException(
                e.getMessage(),
                e.getCause(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        logger.error("Error thrown with status code: " + payload.getHttpStatusCode());

        return new ResponseEntity(
                payload,
                payload.getHttpStatus()
        );
    }
}
