package com.foxminded.racingrating;

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
    void processMustThrowIllegalArgumentExceptionIfPathIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parsingProcessor.process(null);
        });
    }

    @Test
    void processMustThrowDataFormatExceptionIfFileIsEmpty(){
        Path path = Paths.get("src\\test\\resources\\empty.txt");
        List<String> actual = parsingProcessor.process(path);
        List<String> expected = new ArrayList<>();

        assertEquals(actual, expected);
    }


}
