package com.foxminded.racingrating.exceptions;

public class DataFormatException extends RuntimeException {
    public DataFormatException(String input) {
        printMessage(input);
    }

    private void printMessage() {
        System.out.println("Incorrect data in file");
    }

    private void printMessage(String input) {
        System.out.println(input);
    }
}
