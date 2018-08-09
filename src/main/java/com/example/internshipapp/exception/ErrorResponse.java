package com.example.internshipapp.exception;

public class ErrorResponse {

    private String status;

    private Object message;

    public ErrorResponse(String status, Object message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public Object getMessage() {
        return message;
    }
}
