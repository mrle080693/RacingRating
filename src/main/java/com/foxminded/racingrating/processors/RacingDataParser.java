package com.foxminded.racingrating.processors;

import com.foxminded.racingrating.exceptions.DataFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class RacingDataParser {
    private final int ABBREVIATION_LENGTH = 3;
    private final String SEPARATOR = "_";

    public Map<String, LocalTime> getTime(Path path) throws IOException {
        checkTimeFile(path);

        Map<String, LocalTime> result = new HashMap<>();

        Stream<String> lineStream = Files.lines(path);
        lineStream.forEach(line -> {
            String key = line.substring(0, ABBREVIATION_LENGTH);
            String value = line.substring(line.indexOf(SEPARATOR) + 1);
            LocalTime time = LocalTime.parse(value);
            result.put(key, time);
        });

        if (result.isEmpty()) throw new DataFormatException("Sorry ;( File is empty");
        return result;
    }

    public Map<String, String> getNames(Path path) throws IOException {
        return getFromAbbrFile(path, true);
    }

    public Map<String, String> getAutos(Path path) throws IOException {
        return getFromAbbrFile(path, false);
    }

    private Map<String, String> getFromAbbrFile(Path path, Boolean isNames) throws IOException {
        checkAbbrFile(path);

        Map<String, String> result = new HashMap<>();

        Stream<String> lineStream = Files.lines(path);
        lineStream.forEach(line -> {
            String key = line.substring(0, ABBREVIATION_LENGTH);
            String[] splitLine = line.split(SEPARATOR);
            String value;

            if (isNames) {
                value = splitLine[1];
            } else {
                value = splitLine[2];
            }

            result.put(key, value);
        });

        if (result.isEmpty()) throw new DataFormatException("Sorry ;( File is empty");
        return result;
    }

    private void checkTimeFile(Path path) throws IOException {
        checkFile(path, true);
    }

    private void checkAbbrFile(Path path) throws IOException {
        checkFile(path, false);
    }

    private void checkFile(Path path, Boolean isTimeFile) throws IOException {
        if (path == null) throw new IllegalArgumentException("Sorry:( File path is null");

        AtomicReference<Boolean> fileDataFormatIsCorrect = new AtomicReference<>();
        fileDataFormatIsCorrect.set(true);
        AtomicInteger lineNumber = new AtomicInteger();
        lineNumber.set(0);

        Stream<String> lineStream = Files.lines(path);

        lineStream.forEach(line -> {
            lineNumber.getAndIncrement();

            if (isTimeFile) {
                fileDataFormatIsCorrect.set(line
                        .matches(".{3}\\d{4}[-]\\d{2}[-]\\d{2}[_]\\d{2}[:]\\d{2}[:]\\d{2}[.]\\d{3}"));
                if (!fileDataFormatIsCorrect.get()) {
                    throw new DataFormatException("Incorrect data in line number " + lineNumber.get());
                }
            }
            if (!isTimeFile) {
                fileDataFormatIsCorrect.set(line.matches(".{3}[_].+[_].+"));
                if (!fileDataFormatIsCorrect.get()) {
                    throw new DataFormatException("Incorrect data in line number " + lineNumber.get());
                }
            }
        });
    }
}
