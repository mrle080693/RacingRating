package com.foxminded.racingrating.parsers;

import com.foxminded.racingrating.exceptions.DataFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class RacingDataParser {
    public static final int ABBREVIATION_LENGTH = 3;
    public static final String SEPARATOR = "_";
    public static final String PATTERN_TIME = ".{3}\\d{4}[-]\\d{2}[-]\\d{2}[_]\\d{2}[:]\\d{2}[:]\\d{2}[.]\\d{3}";
    public static final String PATTERN_NOT_TIME = ".{3}[_].+[_].+";

    public Map<String, LocalTime> parseTime(Path path) throws IOException {
        Map<String, LocalTime> result = new HashMap<>();

        parseFile(path, false, true).forEach((key, value) -> {
            LocalTime time = LocalTime.parse(value);
            result.put(key, time);
        });

        return result;
    }

    public Map<String, String> parseNames(Path path) throws IOException {
        return parseFile(path, true, false);
    }

    public Map<String, String> parseAutos(Path path) throws IOException {
        return parseFile(path, false, false);
    }

    private Map<String, String> parseFile(Path path, Boolean isNames, Boolean isTime) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Sorry:( File path is null");
        }

        AtomicInteger lineNumber = new AtomicInteger();
        lineNumber.set(0);
        Map<String, String> result = new HashMap<>();
        Stream<String> lineStream = Files.lines(path);

        lineStream.forEach(line -> {
            lineNumber.getAndIncrement();

            if (isTime) {
                checkString(line, PATTERN_TIME, "Incorrect data in line number " + lineNumber.get());

                String key = line.substring(0, ABBREVIATION_LENGTH);
                String value = line.substring(line.indexOf(SEPARATOR) + 1);

                result.put(key, value);
            } else {
                checkString(line, PATTERN_NOT_TIME, "Incorrect data in line number " + lineNumber.get());

                String key = line.substring(0, ABBREVIATION_LENGTH);
                String[] splitLine = line.split(SEPARATOR);
                String value;

                if (isNames) {
                    value = splitLine[1];
                } else {
                    value = splitLine[2];
                }

                result.put(key, value);
            }
        });

        if (result.isEmpty()) {
            throw new DataFormatException("Sorry ;( File is empty");
        }

        return result;
    }

    private void checkString(String input, String pattern, String failMessage) {
        boolean IsCorrect = input.matches(pattern);

        if (!IsCorrect) {
            throw new DataFormatException(failMessage);
        }
    }
}
