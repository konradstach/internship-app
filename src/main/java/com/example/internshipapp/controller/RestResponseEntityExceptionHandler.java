package com.example.internshipapp.controller;

import com.example.internshipapp.exception.ErrorResponse;
import com.example.internshipapp.exception.FieldValidationErrorResponse;
import com.example.internshipapp.exception.NoSuchRecordException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchRecordException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchRecordException(NoSuchRecordException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase() + " (404)", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<FieldValidationErrorResponse> handleNotValidFieldsException(MethodArgumentNotValidException ex) {

        List<FieldValidationErrorResponse> messages = new ArrayList<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        for (int i = 0; i < fieldErrors.size(); i++) {
            messages.add(new FieldValidationErrorResponse(fieldErrors.get(i).getField(), objectErrors.get(i).getDefaultMessage()));
        }
        return messages;
    }
}
