package com.foxminded.racingrating.processors;

import com.foxminded.racingrating.exceptions.DataFormatException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RacingRatingProcessor {
    public static final String PATTERN_FOR_ALL_MAPS_KEYS = ".{3}";
    public static final int RACERS_AMOUNT = 19;
    public static final int COLUMN_SIZE = 33;
    public static final int BEST_RACERS_QUANTITY = 15;

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

        for (String racer : endTimeMap.keySet()) {
            LocalTime start = startTimeMap.get(racer);
            LocalTime end = endTimeMap.get(racer);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            String millisecondsAsString = String.valueOf(milliseconds);
            String resultMilliseconds = millisecondsAsString.substring(millisecondsAsString.length() - 3);

            int seconds = (int) ((milliseconds / 1000) - 60);
            String resultSeconds = String.valueOf(seconds);

            resultSeconds = leftPad(resultSeconds, "0", 2);

            int minutes = (int) ((milliseconds / 1000) / 60);
            String resultMinutes = String.valueOf(minutes);

            String valueResult = resultMinutes + ":" + resultSeconds + "." + resultMilliseconds;

            result.put(racer, valueResult);
        }

        return result;
    }

    private Map<Integer, String> getPositions(Map<String, LocalTime> startTimeMap, Map<String, LocalTime> endTimeMap) {
        Map<String, Integer> resultInMls = new HashMap<>();
        Map<Integer, String> positions = new HashMap<>();

        for (String racer : endTimeMap.keySet()) {
            LocalTime start = startTimeMap.get(racer);
            LocalTime end = endTimeMap.get(racer);
            long milliseconds = start.until(end, ChronoUnit.MILLIS);

            resultInMls.put(racer, (int) milliseconds);
        }

        AtomicInteger position = new AtomicInteger();
        position.set(1);

        resultInMls.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(racer -> {
            positions.put(position.intValue(), racer.getKey());
            position.set(position.intValue() + 1);
        });

        return positions;
    }

    private String[] getRating(Map<String, String> names, Map<String, String> autos,
                               Map<String, String> results, Map<Integer, String> positions) {
        String[] rating = new String[RACERS_AMOUNT];

        for (int i = 0; i < rating.length; i++) {
            int position = i + 1;

            // Variables for rating
            String racersName = names.get(positions.get(position));
            String separatorsAfterName = leftPad("", " ", COLUMN_SIZE - racersName.length());
            String auto = autos.get(positions.get(position));
            String separatorsAfterAuto = leftPad("", " ",
                    COLUMN_SIZE - autos.get(positions.get(position)).length());
            String timeResult = results.get(positions.get(position));

            String racerInRating = position + ". " + racersName + separatorsAfterName + "|" + auto +
                    separatorsAfterAuto + "|" + timeResult;
            rating[i] = racerInRating;
        }

        return rating;
    }

    private String stringArrToString(String[] rating) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < BEST_RACERS_QUANTITY; i++) {
            result.append(rating[i]).append("\n");
        }

        result.append(leftPad("", "-", 80)).append("\n");

        for (int i = BEST_RACERS_QUANTITY; i < rating.length; i++) {
            result.append(rating[i]).append("\n");
        }

        return result.toString().trim();
    }

    private String leftPad(String input, String pad, int finalSize) {
        StringBuilder result = new StringBuilder();

        for (int i = input.length(); i <= finalSize - 1; i++) {
            result.append(pad);
        }
        result.append(input);

        return result.toString();
    }

    private void checkInputs(Map<String, LocalTime> startTimeData, Map<String, LocalTime> endTimeData,
                             Map<String, String> names, Map<String, String> autos) {
        if (startTimeData == null || endTimeData == null || names == null || autos == null) {
            throw new IllegalArgumentException("Map must not be null");
        }

        if (startTimeData.isEmpty() || endTimeData.isEmpty() || names.isEmpty() || autos.isEmpty()) {
            throw new DataFormatException("Map must not be empty");
        }

        checkTimeMap(startTimeData);
        checkTimeMap(endTimeData);
        validateXXX(names);
        validateXXX(autos);
    }

    private void checkTimeMap(Map<String, LocalTime> map) {
        AtomicInteger index = new AtomicInteger();
        index.set(0);

        for (String key : map.keySet()) {
            boolean keyIsCorrect = key.matches(PATTERN_FOR_ALL_MAPS_KEYS);

            if (!keyIsCorrect) {
                throw new DataFormatException("Incorrect map key in index " + index);
            }

            index.set(index.get() + 1);
        }
    }

    private void validateXXX(Map<String, String> map) {
        AtomicInteger index = new AtomicInteger();
        index.set(0);

        for (String key : map.keySet()) {
            checkString(key, PATTERN_FOR_ALL_MAPS_KEYS, "Incorrect map key in index  " + index);

            index.set(index.get() + 1);
        }

        index.set(0);

        map.forEach((key, value) -> {
            final String PATTERN_FOR_MAPS_VALUES = ".+";

            checkString(value, PATTERN_FOR_MAPS_VALUES, "Incorrect map value in index  " + index);

            index.set(index.get() + 1);
        });
    }

    private void checkString(String input, String pattern, String failMessage) {
        boolean IsCorrect = input.matches(pattern);

        if (!IsCorrect) {
            throw new DataFormatException(failMessage);
        }
    }
}
