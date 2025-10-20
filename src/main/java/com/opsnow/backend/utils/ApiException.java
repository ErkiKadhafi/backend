package com.opsnow.backend.utils;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends Exception {
    HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
