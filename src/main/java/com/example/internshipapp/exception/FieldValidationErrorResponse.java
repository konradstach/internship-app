package com.example.internshipapp.exception;

public class FieldValidationErrorResponse {

    private String field;

    private String message;

    public FieldValidationErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
