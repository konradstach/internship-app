package com.example.internshipapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record not found")
public class NoSuchRecordException extends RuntimeException {
    public NoSuchRecordException(String message) {
        super(message);
    }
}
