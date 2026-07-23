package com.example.employeeonboarding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDepartmentReferenceException extends RuntimeException {

    public InvalidDepartmentReferenceException(String message) {
        super(message);
    }
}
