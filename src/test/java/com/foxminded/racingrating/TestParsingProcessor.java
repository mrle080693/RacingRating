package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.FileIsEmptyException;
import com.foxminded.racingrating.exceptions.WrongPathException;
import com.foxminded.racingrating.processors.ParsingProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestParsingProcessor {
    private ParsingProcessor parsingProcessor = new ParsingProcessor();

    @Test
    void parseMustThrowIllegalArgumentExceptionIfPathIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> parsingProcessor.parse(null));
    }

    @Test
    void parseMustReturnFileIsEmptyExceptionIfFileIsEmpty() {
        Path path = Paths.get("src\\test\\resources\\empty.txt");
        Assertions.assertThrows(FileIsEmptyException.class, () -> parsingProcessor.parse(path));
    }

    @Test
    void parseMustReturnWrongPathExceptionIfFilePathIsWrong() {
        Path path = Paths.get("wrong_way.txt");
        Assertions.assertThrows(WrongPathException.class, () -> parsingProcessor.parse(path));
    }

    @Test
    void parseMustReturnCorrectResult() {
        Path path = Paths.get("src\\test\\resources\\test_file.txt");

        List<String> expected = new ArrayList<>();
        expected.add("Hello World!");
        List<String> actual = parsingProcessor.parse(path);

        assertEquals(expected, actual);
    }
}
