package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.DataFormatException;
import com.foxminded.racingrating.processors.RacingDataParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class TestRacingDataParser {
    private RacingDataParser racingDataParser = new RacingDataParser();

    @Test
    void getTimeMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Path path = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser
                .getTime(path));
    }

    @Test
    void getNamesMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Path path = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser
                .getNames(path));
    }

    @Test
    void getAutosMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Path path = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser
                .getAutos(path));
    }

    @Test
    void getTimeMustReturnIOExceptionIfFilePathIsWrong() {
        Path path = Paths.get("Wrong Way");

        Assertions.assertThrows(IOException.class, () -> racingDataParser
                .getTime(path));
    }

    @Test
    void getNamesMustReturnIOExceptionIfFilePathIsWrong() {
        Path path = Paths.get("Wrong Way");

        Assertions.assertThrows(IOException.class, () -> racingDataParser
                .getNames(path));
    }

    @Test
    void getAutosMustReturnIOExceptionIfFilePathIsWrong() {
        Path path = Paths.get("Wrong Way");

        Assertions.assertThrows(IOException.class, () -> racingDataParser
                .getAutos(path));
    }

    @Test
    void getTimeMustReturnDataFormatExceptionIfFileIsEmpty() {
        Path path = Paths.get("src\\test\\resources\\empty_file.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser
                .getTime(path));
    }

    @Test
    void getNamesMustReturnDataFormatExceptionIfFileIsEmpty() {
        Path path = Paths.get("src\\test\\resources\\empty_file.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser
                .getNames(path));
    }

    @Test
    void getAutosMustReturnDataFormatExceptionIfFileIsEmpty() {
        Path path = Paths.get("src\\test\\resources\\empty_file.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser
                .getAutos(path));
    }

    @Test
    void getTimeMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Path path = Paths.get("src\\test\\resources\\test_file.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser
                .getTime(path));
    }

    @Test
    void getNamesMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Path path = Paths.get("src\\test\\resources\\test_file.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser
                .getNames(path));
    }

    @Test
    void getAutosMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Path path = Paths.get("src\\test\\resources\\test_file.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser
                .getAutos(path));
    }
}
