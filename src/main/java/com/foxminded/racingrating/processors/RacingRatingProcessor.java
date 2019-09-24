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

public class RacingRatingProcessor {
    public String process(Path start, Path end, Path abbreviations) {
        Map<String, LocalTime> startTime = null;
        Map<String, LocalTime> endTime = null;
        Map<String, String> names = null;
        Map<String, String> autos = null;
        String result = null;

        try {
            startTime = getStartOrEndTime(start);
            endTime = getStartOrEndTime(end);
            names = getNames(abbreviations);
            autos = getAutos(abbreviations);
        } catch (FileSystemNotFoundException e) {
            throw new FileSystemNotFoundException("Sorry ;( No such file");
        }

        if (startTime != null && endTime != null && names != null && autos != null) {
            Map<String, String> results = getResults(startTime, endTime);
            Map<Integer, String> positions = getPositions(startTime, endTime);

            result = getRating(names, autos, results, positions);
        }

        return result;
    }

    private Map<String, LocalTime> getStartOrEndTime(Path path) {
        Map<String, LocalTime> result = new HashMap<>();

        try (Stream<String> lineStream = Files.lines(path)) {
            lineStream.forEach(i -> {
                String key = i.substring(0, 3);
                String value = i.substring(14);
                LocalTime time = LocalTime.parse(value);
                result.put(key, time);
            });
        } catch (IOException io) {
            System.err.println("Some troubles with file :(");
        }

        return result;
    }

    private Map<String, String> getResults(Map<String, LocalTime> startTime, Map<String, LocalTime> endTime) {
        Map<String, String> result = new HashMap<>();
        endTime.keySet().forEach(i -> {
            LocalTime start = startTime.get(i);
            LocalTime end = endTime.get(i);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            String millisecondsAsString = String.valueOf(milliseconds);
            String resultMilliseconds = millisecondsAsString.substring(millisecondsAsString.length() - 3);

            String resultSeconds = String.valueOf
                    (Integer.valueOf(millisecondsAsString.substring(0, millisecondsAsString.length() - 3)) - 60);

            for (; resultSeconds.length() < 2; ) {
                resultSeconds = "0" + resultSeconds;
            }

            String resultMinutes = String.valueOf
                    (Integer.valueOf(millisecondsAsString.substring(0, millisecondsAsString.length() - 3)) / 60);

            StringBuilder sb = new StringBuilder();
            sb.append(resultMinutes)
                    .append(":")
                    .append(resultSeconds)
                    .append(".")
                    .append(resultMilliseconds);

            String valueResult = sb.toString();

            result.put(i, valueResult);
        });

        return result;
    }

    private Map<String, String> getNames(Path path) {
        Map<String, String> result = new HashMap<>();

        try (Stream<String> lineStream = Files.lines(path)) {
            lineStream.forEach(i -> {
                String key = i.substring(0, 3);

                String value = i.substring(4);
                value = value.substring(0, value.indexOf("_"));

                result.put(key, value);
            });
        } catch (IOException io) {
            System.err.println("Some troubles with file :(");
        }

        return result;
    }

    private Map<String, String> getAutos(Path path) {
        Map<String, String> result = new HashMap<>();

        try (Stream<String> lineStream = Files.lines(path)) {
            lineStream.forEach(i -> {
                String key = i.substring(0, 3);

                String value = i.substring(4);
                value = value.substring(value.indexOf("_") + 1);

                result.put(key, value);
            });
        } catch (IOException io) {
            System.err.println("Some troubles with file :(");
        }

        return result;
    }

    private Map<Integer, String> getPositions(Map<String, LocalTime> startTime, Map<String, LocalTime> endTime) {
        Map<String, Integer> resultInMls = new HashMap<>();
        Map<Integer, String> positions = new HashMap<>();

        endTime.keySet().forEach(i -> {
            LocalTime start = startTime.get(i);
            LocalTime end = endTime.get(i);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            resultInMls.put(i, (int) milliseconds);
        });

        AtomicInteger position = new AtomicInteger();
        position.set(1);
        resultInMls.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(i -> {
                    positions.put(position.intValue(), i.getKey());
                    position.set(position.intValue() + 1);
                });

        return positions;
    }

    private String getRating(Map<String, String> names, Map<String, String> autos,
                             Map<String, String> results, Map<Integer, String> positions) {
        String[] ratingArr = new String[19];
        StringBuilder sb;
        int columnSize = 33;

        for (int i = 0; i < ratingArr.length; i++) {
            sb = new StringBuilder();
            sb.append(i + 1)
                    .append(".")
                    .append(" ")
                    .append(names.get(positions.get(i + 1)))
                    .append(getMultipleInput(" ", columnSize - names.get(positions.get(i + 1)).length()))
                    .append("|")
                    .append(autos.get(positions.get(i + 1)))
                    .append(getMultipleInput(" ", columnSize - autos.get(positions.get(i + 1)).length()))
                    .append("|")
                    .append(results.get(positions.get(i + 1)));

            ratingArr[i] = sb.toString();
        }

        sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append(ratingArr[i])
                    .append("\n");
        }
        sb.append(getMultipleInput("-", 80))
                .append("\n");
        for (int i = 15; i < ratingArr.length; i++) {
            sb.append(ratingArr[i])
                    .append("\n");
        }

        return sb.toString().trim();
    }

    private String getMultipleInput(String input, int amount) {
        StringBuilder result = new StringBuilder();

        for (int i = 1; i <= amount; i++) {
            result.append(input);
        }

        return result.toString();
    }
}
