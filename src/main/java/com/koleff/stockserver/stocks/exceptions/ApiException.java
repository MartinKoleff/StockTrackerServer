package com.koleff.stockserver.stocks.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private String message;
    private Throwable throwable;
    private HttpStatus httpStatus;
    private int httpStatusCode;
    private ZonedDateTime time;

    public ApiException(String message, Throwable throwable, HttpStatus httpStatus, ZonedDateTime time) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatus.value();
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public ZonedDateTime getTime() {
        return time;
    }
}
