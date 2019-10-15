package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.DataFormatException;
import com.foxminded.racingrating.parsers.RacingDataParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRacingDataParser {
    private RacingDataParser racingDataParser = new RacingDataParser();

    private final Path WRONG_PATH = Paths.get("Wrong Way");
    private final Path TEST_EMPTY_FILE_PATH = Paths.get("src" + File.separator + "test" + File.separator + "resources" +
            File.separator + "test_empty.txt");
    private final Path TEST_WRONG_DATA_PATH = Paths.get("src" + File.separator + "test" + File.separator + "resources" +
            File.separator + "test_wrong_data.txt");
    private final Path TEST_CORRECT_ABBREVIATIONS_PATH = Paths.get("src" + File.separator + "test" + File.separator + "resources" +
            File.separator + "test_correct_abbr.txt");

    @Test
    void parseTimeMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser.parseTime(null));
    }

    @Test
    void parseNamesMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser.parseNames(null));
    }

    @Test
    void parseAutosMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingDataParser.parseAutos(null));
    }

    @Test
    void parseTimeMustReturnIOExceptionIfFilePathIsWrong() {
        Assertions.assertThrows(IOException.class, () -> racingDataParser.parseTime(WRONG_PATH));
    }

    @Test
    void parseNamesMustReturnIOExceptionIfFilePathIsWrong() {
        Assertions.assertThrows(IOException.class, () -> racingDataParser.parseNames(WRONG_PATH));
    }

    @Test
    void parseAutosMustReturnIOExceptionIfFilePathIsWrong() {
        Assertions.assertThrows(IOException.class, () -> racingDataParser.parseAutos(WRONG_PATH));
    }

    @Test
    void parseTimeMustReturnDataFormatExceptionIfFileIsEmpty() {
        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseTime(TEST_EMPTY_FILE_PATH));
    }

    @Test
    void parseNamesMustReturnDataFormatExceptionIfFileIsEmpty() {
        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseNames(TEST_EMPTY_FILE_PATH));
    }

    @Test
    void parseAutosMustReturnDataFormatExceptionIfFileIsEmpty() {
        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseAutos(TEST_EMPTY_FILE_PATH));
    }

    @Test
    void parseTimeMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseTime(TEST_WRONG_DATA_PATH));
    }

    @Test
    void parseNamesMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseNames(TEST_WRONG_DATA_PATH));
    }

    @Test
    void parseAutosMustReturnDataFormatExceptionIfFileDataFormatIsIncorrect() {
        Assertions.assertThrows(DataFormatException.class, () -> racingDataParser.parseAutos(TEST_WRONG_DATA_PATH));
    }

    @Test
    void parseTimeMustReturnCorrectResult() throws IOException {
        Map<String, LocalTime> expected = new HashMap<>();
        expected.put("MES", LocalTime.parse("12:05:58.778"));

        Path path = Paths.get("src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "test_correct_time.txt");
        Map<String, LocalTime> actual = racingDataParser.parseTime(path);

        assertEquals(expected, actual);

    }

    @Test
    void parseNamesMustReturnCorrectResult() throws IOException {
        Map<String, String> expected = new HashMap<>();
        expected.put("DRR", "Daniel Ricciardo");

        Map<String, String> actual = racingDataParser.parseNames(TEST_CORRECT_ABBREVIATIONS_PATH);

        assertEquals(expected, actual);
    }

    @Test
    void parseAutosMustReturnCorrectResult() throws IOException {
        Map<String, String> expected = new HashMap<>();
        expected.put("DRR", "RED BULL RACING TAG HEUER");

        Map<String, String> actual = racingDataParser.parseAutos(TEST_CORRECT_ABBREVIATIONS_PATH);

        assertEquals(expected, actual);
    }
}
