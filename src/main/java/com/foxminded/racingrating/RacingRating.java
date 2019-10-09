package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.DataFormatException;
import com.foxminded.racingrating.processors.RacingDataParser;
import com.foxminded.racingrating.processors.RacingRatingProcessor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Map;

public class RacingRating {
    public static void main(String[] args) {
        final Path PATH_TO_START_FILE = Paths.get("start.log");
        final Path PATH_TO_END_FILE = Paths.get("end.log");
        final Path PATH_TO_ABBREVIATIONS_FILE = Paths.get("abbreviations.txt");

        try {
            RacingDataParser racingDataParser = new RacingDataParser();
            Map<String, LocalTime> startTimeMap = racingDataParser.getTime(PATH_TO_START_FILE);
            Map<String, LocalTime> endTimeMap = racingDataParser.getTime(PATH_TO_END_FILE);
            Map<String, String> names = racingDataParser.getNames(PATH_TO_ABBREVIATIONS_FILE);
            Map<String, String> autos = racingDataParser.getAutos(PATH_TO_ABBREVIATIONS_FILE);

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
