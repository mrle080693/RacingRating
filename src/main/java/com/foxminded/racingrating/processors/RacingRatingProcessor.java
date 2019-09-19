package com.foxminded.racingrating.processors;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RacingRatingProcessor {
    public String process(Path start, Path end, Path abbreviations){
        String result = null;

        List<String> startList = fileToList(start);
        List<String> endList = fileToList(end);
        List<String> abbreviationsList = fileToList(abbreviations);

        return result;
    }

   private List<String> fileToList(Path path){
        List<String> result = new ArrayList<>();

       try (Stream<String> lineStream = Files.lines(path)) {

           result = lineStream.collect(Collectors.toList());

       } catch (IOException io) {
           System.err.println("Some troubles with file :(");
       }

       return result;
   }
}
