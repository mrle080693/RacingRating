package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.DataFormatException;
import com.foxminded.racingrating.parsers.RacingDataParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRacingDataParser {
    private RacingDataParser racingDataParser = new RacingDataParser();

    @Test
    void parseTimeMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Path path = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser.parseTime(path));
    }

    @Test
    void parseNamesMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Path path = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser.parseNames(path));
    }

    @Test
    void parseAutosMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Path path = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser.parseAutos(path));
    }

    @Test
    void parseTimeMustReturnIOExceptionIfFilePathIsWrong() {
        Path path = Paths.get("Wrong Way");

        Assertions.assertThrows(IOException.class, () -> racingDataParser.parseTime(path));
    }

    @Test
    void parseNamesMustReturnIOExceptionIfFilePathIsWrong() {
        Path path = Paths.get("Wrong Way");

        Assertions.assertThrows(IOException.class, () -> racingDataParser.parseNames(path));
    }

    @Test
    void parseAutosMustReturnIOExceptionIfFilePathIsWrong() {
        Path path = Paths.get("Wrong Way");

        Assertions.assertThrows(IOException.class, () -> racingDataParser.parseAutos(path));
    }

    @Test
    void parseTimeMustReturnDataFormatExceptionIfFileIsEmpty() {
        Path path = Paths.get("src\\test\\resources\\test_empty.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseTime(path));
    }

    @Test
    void parseNamesMustReturnDataFormatExceptionIfFileIsEmpty() {
        Path path = Paths.get("src\\test\\resources\\test_empty.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseNames(path));
    }

    @Test
    void parseAutosMustReturnDataFormatExceptionIfFileIsEmpty() {
        Path path = Paths.get("src\\test\\resources\\test_empty.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseAutos(path));
    }

    @Test
    void parseTimeMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Path path = Paths.get("src\\test\\resources\\test_wrong_data.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseTime(path));
    }

    @Test
    void parseNamesMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Path path = Paths.get("src\\test\\resources\\test_wrong_data.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseNames(path));
    }

    @Test
    void parseAutosMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Path path = Paths.get("src\\test\\resources\\test_wrong_data.txt");

        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseAutos(path));
    }

    @Test
    void parseTimeMustReturnCorrectResult() throws IOException {
        Map<String, LocalTime> expected = new HashMap<>();
        expected.put("MES", LocalTime.parse("12:05:58.778"));

        Path path = Paths.get("src\\test\\resources\\test_correct_time.txt");
        Map<String, LocalTime> actual = racingDataParser.parseTime(path);

        assertEquals(expected, actual);

    }

    @Test
    void parseNamesMustReturnCorrectResult() throws IOException {
        Map<String, String> expected = new HashMap<>();
        expected.put("DRR", "Daniel Ricciardo");

        Path path = Paths.get("src\\test\\resources\\test_correct_abbr.txt");
        Map<String, String> actual = racingDataParser.parseNames(path);

        assertEquals(expected, actual);
    }

    @Test
    void parseAutosMustReturnCorrectResult() throws IOException {
        Map<String, String> expected = new HashMap<>();
        expected.put("DRR", "RED BULL RACING TAG HEUER");

        Path path = Paths.get("src\\test\\resources\\test_correct_abbr.txt");
        Map<String, String> actual = racingDataParser.parseAutos(path);

        assertEquals(expected, actual);
    }
}
