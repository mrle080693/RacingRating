package com.foxminded.racingrating;

import com.foxminded.racingrating.processors.RacingRatingProcessor;

import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RacingRating {
    public static void main(String[] args) {
        try {
            Path start = Paths.get("start.log");
            Path end = Paths.get("end.log");
            Path abbreviations = Paths.get("abbreviations.txt");

            RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();
            String result = racingRatingProcessor.process(start, end, abbreviations);

            System.out.println(result);
        } catch (FileSystemNotFoundException e) {
            System.err.println("Sorry ;( No such file");
        }
    }
}
