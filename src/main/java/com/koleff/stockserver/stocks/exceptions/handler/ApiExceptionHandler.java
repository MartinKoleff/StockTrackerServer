package com.koleff.stockserver.stocks.exceptions.handler;

import com.koleff.stockserver.stocks.exceptions.*;
import com.koleff.stockserver.stocks.utils.dateUtil.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    private final static Logger logger = LogManager.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {

        //Returned to the client
        ApiException payload = new ApiException(
                e.getMessage(),
                e.getCause(),
                HttpStatus.BAD_REQUEST,
                DateUtil.toISO8601(LocalDateTime.now())
        );

        logger.error("Error thrown with status code: " + payload.getHttpStatusCode());

        return new ResponseEntity<>(
                payload,
                payload.getHttpStatus()
        );
    }

    @ExceptionHandler(value = {
            CurrenciesNotFoundException.class,
            CurrencyNotFoundException.class,
            EndOfDayNotFoundException.class,
            IntraDayNotFoundException.class,
            StockExchangeNotFoundException.class,
            StockNotFoundException.class,
            StocksNotFoundException.class,
            TimezoneNotFoundException.class,
            TimezonesNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFoundException(RuntimeException e) {

        //Returned to the client
        ApiException payload = new ApiException(
                e.getMessage(),
                e.getCause(),
                HttpStatus.NOT_FOUND,
                DateUtil.toISO8601(LocalDateTime.now())
        );

        logger.error("Error thrown with status code: " + payload.getHttpStatusCode());

        return new ResponseEntity<>(
                payload,
                payload.getHttpStatus()
        );
    }

    @ExceptionHandler(value = {
            DataNotSavedException.class,
            IntraDayNotSavedException.class,
            //TODO: add for each entity...
    })
    public ResponseEntity<Object> handleNotSavedException(RuntimeException e) {

        //Returned to the client
        ApiException payload = new ApiException(
                e.getMessage(),
                e.getCause(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                DateUtil.toISO8601(LocalDateTime.now())
        );

        logger.error("Error thrown with status code: " + payload.getHttpStatusCode());

        return new ResponseEntity<>(
                payload,
                payload.getHttpStatus()
        );
    }

    @ExceptionHandler(value = DBEmptyException.class)
    public ResponseEntity<Object> handleDBEmptyException(DBEmptyException e) {

        //Returned to the client
        ApiException payload = new ApiException(
                e.getMessage(),
                e.getCause(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                DateUtil.toISO8601(LocalDateTime.now())
        );

        logger.error("Error thrown with status code: " + payload.getHttpStatusCode());

        return new ResponseEntity<>(
                payload,
                payload.getHttpStatus()
        );
    }

    @ExceptionHandler(value = JsonNotFoundException.class)
    public ResponseEntity<Object> handleJsonNotFoundException(JsonNotFoundException e) {

        //Returned to the client
        ApiException payload = new ApiException(
                e.getMessage(),
                e.getCause(),
                HttpStatus.NOT_FOUND,
                DateUtil.toISO8601(LocalDateTime.now())
        );

        logger.error("Error thrown with status code: " + payload.getHttpStatusCode());

        return new ResponseEntity<>(
                payload,
                payload.getHttpStatus()
        );
    }

    //TODO: add success status.ok
}
