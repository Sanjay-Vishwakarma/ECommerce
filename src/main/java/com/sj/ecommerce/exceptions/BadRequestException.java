package com.sj.ecommerce.exceptions;

import org.springframework.http.HttpStatus;


public final class BadRequestException extends RuntimeException {

    private String code;
    private String message;

    private HttpStatus httpStatus;

    public BadRequestException(String code, String message, HttpStatus httpStatus) {
        super(message + code + message);
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }


    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}