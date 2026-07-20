package com.example.employeeonboarding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LastNameUpdateNotAllowedException extends RuntimeException {

    public LastNameUpdateNotAllowedException(String message) {
        super(message);
    }
}
