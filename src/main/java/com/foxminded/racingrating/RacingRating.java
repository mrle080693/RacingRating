package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.DataFormatException;
import com.foxminded.racingrating.parsers.RacingDataParser;
import com.foxminded.racingrating.processors.RacingRatingProcessor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Map;

public class RacingRating {

    private static final Path START_LOG_FILE_PATH = Paths.get("start.log");
    private static final Path END_LOG_FILE_PATH = Paths.get("end.log");
    private static final Path ABBREVIATIONS_TXT_FILE_PATH = Paths.get("abbreviations.txt");

    public static void main(String[] args) {
        try {
            RacingDataParser racingDataParser = new RacingDataParser();
            Map<String, LocalTime> startTimeMap = racingDataParser.parseTime(START_LOG_FILE_PATH);
            Map<String, LocalTime> endTimeMap = racingDataParser.parseTime(END_LOG_FILE_PATH);
            Map<String, String> names = racingDataParser.parseNames(ABBREVIATIONS_TXT_FILE_PATH);
            Map<String, String> autos = racingDataParser.parseAutos(ABBREVIATIONS_TXT_FILE_PATH);

            RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();
            String result = racingRatingProcessor.process(startTimeMap, endTimeMap, names, autos);

            System.out.println(result);
        } catch (IllegalArgumentException | DataFormatException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Sorry ;( Connection troubles");
        }
    }
}
