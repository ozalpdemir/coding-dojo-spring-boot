package com.assignment.spring.openweather;

public class IncorrectResponseException extends RuntimeException {

    private static final long serialVersionUID = -2135527795395353617L;

    public IncorrectResponseException(String errorMessage) {
        super(errorMessage);
    }
}