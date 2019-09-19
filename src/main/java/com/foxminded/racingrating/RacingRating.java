package com.foxminded.racingrating;

import com.foxminded.racingrating.processors.RacingRatingProcessor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RacingRating {
    public static void main(String[] args) {
        RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();
        Path start = Paths.get("start.log");
        Path end = Paths.get("end.log");
        Path abbreviations = Paths.get("abbreviations.txt");
        String result = racingRatingProcessor.process(start, end, abbreviations);
    }
}
