package com.foxminded.racingrating.processors;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

public class RacingRatingProcessor {
    public String process(Path pathToStartFile, Path pathToEndFile, Path pathToAbbreviationsFile) {
        Map<String, LocalTime> startTime = null;
        Map<String, LocalTime> endTime = null;
        Map<String, String> names = null;
        Map<String, String> autos = null;
        String result = null;

        try {
            startTime = getTime(pathToStartFile);
            endTime = getTime(pathToEndFile);
            names = getFromAbbreviationsFile(pathToAbbreviationsFile, true);
            autos = getFromAbbreviationsFile(pathToAbbreviationsFile, false);
        } catch (FileSystemNotFoundException e) {
            throw new FileSystemNotFoundException("Sorry ;( No such file");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Sorry ;( File path cant be null");
        } catch (IOException io) {
            try {
                throw new DataFormatException("Sorry :( Some troubles with file");
            } catch (DataFormatException e) {
                System.err.println("Sorry :( Some troubles with file");
            }
        }

        try {
            Map<String, String> results = getResults(startTime, endTime);
            Map<Integer, String> positions = getPositions(startTime, endTime);

            String[] rating = getRating(names, autos, results, positions);
            result = stringArrToString(rating);
        } catch (NullPointerException npe) {
            throw new IllegalArgumentException("Sorry ;(");
        }

        return result;
    }

    private Map<String, LocalTime> getTime(Path path) throws IOException {
        Map<String, LocalTime> result = new HashMap<>();

        Stream<String> lineStream = Files.lines(path);
        lineStream.forEach(line -> {
            String key = line.substring(0, 3);
            String value = line.substring(14);
            LocalTime time = LocalTime.parse(value);
            result.put(key, time);
        });

        return result;
    }

    private Map<String, String> getResults(Map<String, LocalTime> startTime, Map<String, LocalTime> endTime) {
        Map<String, String> result = new HashMap<>();
        endTime.keySet().forEach(racer -> {
            LocalTime start = startTime.get(racer);
            LocalTime end = endTime.get(racer);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            String millisecondsAsString = String.valueOf(milliseconds);
            String resultMilliseconds = millisecondsAsString.substring(millisecondsAsString.length() - 3);

            int seconds = Integer.valueOf(millisecondsAsString.substring(0, millisecondsAsString.length() - 3)) - 60;
            StringBuilder resultSeconds = new StringBuilder(String.valueOf(seconds));

            for (; resultSeconds.length() < 2; ) {
                resultSeconds.insert(0, "0");
            }

            int minutes = Integer.valueOf(millisecondsAsString.substring(0, millisecondsAsString.length() - 3)) / 60;
            String resultMinutes = String.valueOf(minutes);

            String valueResult = resultMinutes + ":" + resultSeconds + "." + resultMilliseconds;

            result.put(racer, valueResult);
        });

        return result;
    }

    private Map<String, String> getFromAbbreviationsFile(Path path, Boolean names) throws IOException {
        Map<String, String> result = new HashMap<>();

        Stream<String> lineStream = Files.lines(path);
        lineStream.forEach(line -> {
            final String SEPARATOR = "_";
            final int ABBREVIATION_LENGTH = 3;
            String key = line.substring(0, ABBREVIATION_LENGTH);

            String value = line.substring(ABBREVIATION_LENGTH + 1);
            if (names) {
                value = value.substring(0, value.indexOf(SEPARATOR));
            } else {
                value = value.substring(value.indexOf(SEPARATOR) + 1);
            }
            result.put(key, value);
        });

        return result;
    }

    private Map<Integer, String> getPositions(Map<String, LocalTime> startTime, Map<String, LocalTime> endTime) {
        Map<String, Integer> resultInMls = new HashMap<>();
        Map<Integer, String> positions = new HashMap<>();

        endTime.keySet().forEach(racer -> {
            LocalTime start = startTime.get(racer);
            LocalTime end = endTime.get(racer);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            resultInMls.put(racer, (int) milliseconds);
        });

        AtomicInteger position = new AtomicInteger();
        position.set(1);
        resultInMls.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(racer -> {
                    positions.put(position.intValue(), racer.getKey());
                    position.set(position.intValue() + 1);
                });

        return positions;
    }

    private String[] getRating(Map<String, String> names, Map<String, String> autos,
                               Map<String, String> results, Map<Integer, String> positions) {
        String[] rating = new String[19];
        int columnSize = 33;

        for (int i = 0; i < rating.length; i++) {
            int position = i + 1;

            String racerInRating = position + "." + " " + names.get(positions.get(position)) +
                    getMultipleInput(" ", columnSize - names.get(positions.get(position)).length()) +
                    "|" + autos.get(positions.get(position)) +
                    getMultipleInput(" ", columnSize - autos.get(positions.get(position)).length()) +
                    "|" + results.get(positions.get(position));
            rating[i] = racerInRating;
        }

        return rating;
    }

    private String stringArrToString(String[] rating) {
        StringBuilder resultBuilder = new StringBuilder();
        final int BEST_RACERS_QUANTITY = 15;

        for (int i = 0; i < BEST_RACERS_QUANTITY; i++) {
            resultBuilder.append(rating[i])
                    .append("\n");
        }
        resultBuilder.append(getMultipleInput("-", 80))
                .append("\n");
        for (int i = BEST_RACERS_QUANTITY; i < rating.length; i++) {
            resultBuilder.append(rating[i])
                    .append("\n");
        }

        return resultBuilder.toString().trim();
    }

    private String getMultipleInput(String input, int amount) {
        StringBuilder result = new StringBuilder();

        for (int i = 1; i <= amount; i++) {
            result.append(input);
        }

        return result.toString();
    }
}
