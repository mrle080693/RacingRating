package com.foxminded.racingrating.exceptions;

public class FileIsEmptyException extends RuntimeException {

    public FileIsEmptyException() {
        getMessage();
    }

    public String getMessage() {
        return "Sorry, File is empty ;(";
    }
}
