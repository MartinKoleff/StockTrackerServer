package com.koleff.stockserver.stocks.exceptions;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


public @Data class ApiException { //TODO: rename to DTO?
    @SerializedName("message")
    private final String message;

    @SerializedName("throwable")
    private final Throwable throwable;

    @SerializedName("http_status")
    private final HttpStatus httpStatus;

    @SerializedName("http_status_code")
    private final int httpStatusCode;

    @SerializedName("time")
    private final ZonedDateTime time;

    public ApiException(String message, Throwable throwable, HttpStatus httpStatus, ZonedDateTime time) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatus.value();
        this.time = time;
    }
}

