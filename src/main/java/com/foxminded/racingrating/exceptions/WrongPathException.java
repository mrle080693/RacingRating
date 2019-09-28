package com.foxminded.racingrating.exceptions;

public class WrongPathException extends RuntimeException {
    public WrongPathException(){
        getMessage();
    }

    public String getMessage() {
        return "Sorry, Wrong file path ;(";
    }
}
