package com.foxminded.racingrating.parsers;

import com.foxminded.racingrating.exceptions.DataFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RacingDataParser {
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

        int lineNumber = 0;
        Map<String, String> result = new HashMap<>();
        Stream<String> lineStream = Files.lines(path);
        Object[] lines = lineStream.toArray();

        for (Object obj : lines) {
            String line = obj.toString();
            lineNumber++;

            int ABBREVIATION_LENGTH = 3;
            String SEPARATOR = "_";
            if (isTime) {
                String CHECK_TIME_FILES_DATA_PATTERN = ".{3}\\d{4}[-]\\d{2}[-]\\d{2}[_]\\d{2}[:]\\d{2}[:]\\d{2}[.]\\d{3}";
                checkString(line, CHECK_TIME_FILES_DATA_PATTERN, "Incorrect data in line number "
                        + lineNumber);

                String key = line.substring(0, ABBREVIATION_LENGTH);
                String value = line.substring(line.indexOf(SEPARATOR) + 1);

                result.put(key, value);

            } else {
                String CHECK_ABBREVIATION_FILE_DATA_PATTERN = ".{3}[_].+[_].+";
                checkString(line, CHECK_ABBREVIATION_FILE_DATA_PATTERN, "Incorrect data in line number "
                        + lineNumber);

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
        }

        if (result.isEmpty()) {
            throw new DataFormatException("Sorry ;( File is empty");
        }

        return result;
    }

    private void checkString(String input, String pattern, String failMessage) {
        boolean isCorrect = input.matches(pattern);

        if (!isCorrect) {
            throw new DataFormatException(failMessage);
        }
    }
}
