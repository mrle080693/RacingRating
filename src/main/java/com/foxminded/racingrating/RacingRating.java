package com.foxminded.racingrating;

import com.foxminded.racingrating.processors.RacingRatingProcessor;

import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RacingRating {
    public static void main(String[] args) {
        try {
            Path pathToStartFile = Paths.get("start.log");
            Path pathToEndFile = Paths.get("end.log");
            Path pathToAbbreviationsFile = Paths.get("abbreviations.txt");

            RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();
            String result = racingRatingProcessor.process(pathToStartFile, pathToEndFile, pathToAbbreviationsFile);

            System.out.println(result);
        } catch (FileSystemNotFoundException e) {
            System.err.println("Sorry ;( No such file");
        }
    }
}
