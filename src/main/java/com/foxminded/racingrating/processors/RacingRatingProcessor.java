package com.foxminded.racingrating.processors;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RacingRatingProcessor {
    public String process(List<String> startTimeList, List<String> endTimeList, List<String> abbreviationsList) {
        Map<String, LocalTime> startTimeMap = getTime(startTimeList);
        Map<String, LocalTime> endTimeMap = getTime(endTimeList);
        Map<String, String> names = getFromAbbreviationsList(abbreviationsList, true);
        Map<String, String> autos = getFromAbbreviationsList(abbreviationsList, false);

        Map<String, String> results = getResults(startTimeMap, endTimeMap);
        Map<Integer, String> positions = getPositions(startTimeMap, endTimeMap);

        String[] rating = getRating(names, autos, results, positions);

        return stringArrToString(rating);
    }

    private Map<String, LocalTime> getTime(List<String> list) {
        Map<String, LocalTime> result = new HashMap<>();

        list.forEach(line -> {
            String key = line.substring(0, 3);
            String value = line.substring(14);
            LocalTime time = LocalTime.parse(value);
            result.put(key, time);
        });

        return result;
    }

    private Map<String, String> getFromAbbreviationsList(List<String> list, Boolean names) {
        Map<String, String> result = new HashMap<>();

        list.forEach(line -> {
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

    private Map<String, String> getResults(Map<String, LocalTime> startTimeMap, Map<String, LocalTime> endTimeMap) {
        Map<String, String> result = new HashMap<>();
        endTimeMap.keySet().forEach(racer -> {
            LocalTime start = startTimeMap.get(racer);
            LocalTime end = endTimeMap.get(racer);
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

    private Map<Integer, String> getPositions(Map<String, LocalTime> startTimeMap, Map<String, LocalTime> endTimeMap) {
        Map<String, Integer> resultInMls = new HashMap<>();
        Map<Integer, String> positions = new HashMap<>();

        endTimeMap.keySet().forEach(racer -> {
            LocalTime start = startTimeMap.get(racer);
            LocalTime end = endTimeMap.get(racer);
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
