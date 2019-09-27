package com.foxminded.racingrating;

import com.foxminded.racingrating.processors.ParsingProcessor;
import com.foxminded.racingrating.processors.RacingRatingProcessor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RacingRating {
    public static void main(String[] args) {
        try {
            Path pathToStartFile = Paths.get("src\\main\\resources\\start.log");
            Path pathToEndFile = Paths.get("src\\main\\resources\\end.log");
            Path pathToAbbreviationsFile = Paths.get("src\\main\\resources\\abbreviations.txt");

            ParsingProcessor parsingProcessor = new ParsingProcessor();
            List<String> startTimeList = parsingProcessor.process(pathToStartFile);
            List<String> endTimeList = parsingProcessor.process(pathToEndFile);
            List<String> abbreviationsList = parsingProcessor.process(pathToAbbreviationsFile);

            RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();
            String result = racingRatingProcessor.process(startTimeList, endTimeList, abbreviationsList);

            System.out.println(result);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
    }
}
