package com.foxminded.racingrating.processors;

import com.foxminded.racingrating.exceptions.DataFormatException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RacingRatingProcessor {
    private final String PATTERN_FOR_ALL_MAPS_KEYS = ".{3}";

    public String process(Map<String, LocalTime> startTimeMap, Map<String, LocalTime> endTimeMap,
                          Map<String, String> names, Map<String, String> autos) {
        checkInputs(startTimeMap, endTimeMap, names, autos);

        Map<String, String> results = getResults(startTimeMap, endTimeMap);
        Map<Integer, String> positions = getPositions(startTimeMap, endTimeMap);

        String[] rating = getRating(names, autos, results, positions);

        return stringArrToString(rating);
    }

    private Map<String, String> getResults(Map<String, LocalTime> startTimeMap, Map<String, LocalTime> endTimeMap) {
        Map<String, String> result = new HashMap<>();

        endTimeMap.keySet().forEach(racer -> {
            LocalTime start = startTimeMap.get(racer);
            LocalTime end = endTimeMap.get(racer);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            String millisecondsAsString = String.valueOf(milliseconds);
            String resultMilliseconds = millisecondsAsString.substring(millisecondsAsString.length() - 3);

            int seconds = (int) ((milliseconds / 1000) - 60);
            String resultSeconds = String.valueOf(seconds);

            resultSeconds = lPad(resultSeconds, "0", 2);


            int minutes = (int) ((milliseconds / 1000) / 60);
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
        final int RACERS_AMOUNT = 19;
        String[] rating = new String[RACERS_AMOUNT];
        int columnSize = 33;

        for (int i = 0; i < rating.length; i++) {
            int position = i + 1;

            String racerInRating = position + "." + " " + names.get(positions.get(position)) +
                    lPad("", " ", columnSize - names.get(positions.get(position)).length()) +
                    "|" + autos.get(positions.get(position)) +
                    lPad("", " ", columnSize - autos.get(positions.get(position)).length()) +
                    "|" + results.get(positions.get(position));
            rating[i] = racerInRating;
        }

        return rating;
    }

    private String stringArrToString(String[] rating) {
        StringBuilder result = new StringBuilder();
        final int BEST_RACERS_QUANTITY = 15;
        final String underline = "-";

        for (int i = 0; i < BEST_RACERS_QUANTITY; i++) {
            result.append(rating[i]).append("\n");
        }

        result.append(lPad("", underline, 80)).append("\n");

        for (int i = BEST_RACERS_QUANTITY; i < rating.length; i++) {
            result.append(rating[i]).append("\n");
        }

        return result.toString().trim();
    }

    private String lPad(String input, String plusIt, int finalSize) {
        StringBuilder result = new StringBuilder();

        for (int i = input.length(); i <= finalSize - 1; i++) {
            result.append(plusIt);
        }
        result.append(input);

        return result.toString();
    }

    private void checkInputs(Map<String, LocalTime> startTimeMap, Map<String, LocalTime> endTimeMap,
                             Map<String, String> names, Map<String, String> autos) {
        if (startTimeMap == null || endTimeMap == null || names == null || autos == null) {
            throw new IllegalArgumentException("Map must not be null");
        }

        if (startTimeMap.isEmpty() || endTimeMap.isEmpty() || names.isEmpty() || autos.isEmpty()) {
            throw new DataFormatException("Map must not be empty");
        }

        checkTimeMap(startTimeMap);
        checkTimeMap(endTimeMap);
        checkNamesAutosMap(names);
        checkNamesAutosMap(autos);
    }

    private void checkTimeMap(Map<String, LocalTime> map) {
        AtomicInteger index = new AtomicInteger();
        index.set(0);

        map.keySet().forEach(key -> {
            boolean keyIsCorrect = key.matches(PATTERN_FOR_ALL_MAPS_KEYS);
            if (!keyIsCorrect) throw new DataFormatException("Incorrect map key in index " + index);
            index.set(index.get() + 1);
        });
    }

    private void checkNamesAutosMap(Map<String, String> map) {
        AtomicInteger index = new AtomicInteger();
        index.set(0);

        map.keySet().forEach(key -> {
            boolean keyIsCorrect = key.matches(PATTERN_FOR_ALL_MAPS_KEYS);
            if (!keyIsCorrect) throw new DataFormatException("Incorrect map key in index  " + index);
            index.set(index.get() + 1);
        });

        index.set(0);

        map.forEach((key, value) -> {
            boolean keyIsCorrect = value.matches(".+");
            if (!keyIsCorrect) throw new DataFormatException("Incorrect map value in index " + index);
            index.set(index.get() + 1);
        });
    }
}
