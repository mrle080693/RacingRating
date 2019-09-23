package com.foxminded.racingrating.processors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class RacingRatingProcessor {
    public String process(Path start, Path end, Path abbreviations) {
        String result = null;

        Map<String, LocalTime> startTime = getStartOrEndTime(start);
        Map<String, LocalTime> endTime = getStartOrEndTime(end);

        Map<String, String> results = getResults(startTime, endTime);
        Map<String, Integer> positions = getPositions(startTime, endTime);
        Map<String, String> names = getNames(abbreviations);
        Map<String, String> autos = getAutos(abbreviations);

        result = getRating(names, autos, results, positions);

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


    // Переписываем на TreeMap (Сортируем расположение по значениям в resultInMls, а потом просто меняем значение ++)
    private Map<String, Integer> getPositions(Map<String, LocalTime> startTime, Map<String, LocalTime> endTime) {
        Map<String, Integer> resultInMls = new HashMap<>();
        Map<String, Integer> positions = new HashMap<>();

        endTime.keySet().forEach(i -> {
            LocalTime start = startTime.get(i);
            LocalTime end = endTime.get(i);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            resultInMls.put(i, (int) milliseconds);
        });

        return positions;
    }

    private String getRating(Map<String, String> names, Map<String, String> autos,
                             Map<String, String> results, Map<String, Integer> positions){

        String result = null;

        for(int i = 0; i < names.size(); i++){

        }

        return result;
    }
}
