package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.FileIsEmptyException;
import com.foxminded.racingrating.exceptions.WrongPathException;
import com.foxminded.racingrating.processors.ParsingProcessor;
import com.foxminded.racingrating.processors.RacingRatingProcessor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RacingRating {
    public static void main(String[] args) {
        Path pathToStartFile = Paths.get("src\\main\\resources\\start.log");
        Path pathToEndFile = Paths.get("src\\main\\resources\\end.log");
        Path pathToAbbreviationsFile = Paths.get("src\\main\\resources\\abbreviations.txt");

        try {
            ParsingProcessor parsingProcessor = new ParsingProcessor();
            List<String> startTimeList = parsingProcessor.parse(pathToStartFile);
            List<String> endTimeList = parsingProcessor.parse(pathToEndFile);
            List<String> abbreviationsList = parsingProcessor.parse(pathToAbbreviationsFile);

            RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();
            String result = racingRatingProcessor.process(startTimeList, endTimeList, abbreviationsList);
            System.out.println(result);

        } catch (IllegalArgumentException | FileIsEmptyException | WrongPathException e) {
            System.err.println(e.getMessage());
        }
    }
}
