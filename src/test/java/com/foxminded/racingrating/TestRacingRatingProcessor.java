package com.foxminded.racingrating;

import com.foxminded.racingrating.processors.ParsingProcessor;
import com.foxminded.racingrating.processors.RacingRatingProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRacingRatingProcessor {
    private RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();
    private ParsingProcessor parsingProcessor = new ParsingProcessor();

    private Path pathToStartFile = Paths.get("src\\main\\resources\\start.log");
    private Path pathToEndFile = Paths.get("src\\main\\resources\\end.log");
    private Path pathToAbbreviationsFile = Paths.get("src\\main\\resources\\abbreviations.txt");

    private List<String> startTimeList = parsingProcessor.parse(pathToStartFile);
    private List<String> endTimeList = parsingProcessor.parse(pathToEndFile);
    private List<String> abbreviationsList = parsingProcessor.parse(pathToAbbreviationsFile);

    @Test
    void processMustReturnIllegalArgumentExceptionIfInputIsNull() {
        // If startTimeList == null
        startTimeList = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeList, endTimeList, abbreviationsList));

        startTimeList = parsingProcessor.parse(pathToStartFile);

        // If endTimeList == null
        endTimeList = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeList, endTimeList, abbreviationsList));

        endTimeList = parsingProcessor.parse(pathToEndFile);

        // If abbreviationsList == null
        abbreviationsList = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeList, endTimeList, abbreviationsList));

        abbreviationsList = parsingProcessor.parse(pathToAbbreviationsFile);
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfInputIsEmpty() {
        // If startTimeList is empty
        startTimeList = new ArrayList<>();

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeList, endTimeList, abbreviationsList));

        startTimeList = parsingProcessor.parse(pathToStartFile);

        // If endTimeList is empty
        endTimeList = new ArrayList<>();

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeList, endTimeList, abbreviationsList));

        endTimeList = parsingProcessor.parse(pathToEndFile);

        // If abbreviationsList is empty
        abbreviationsList = new ArrayList<>();

        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeList, endTimeList, abbreviationsList));

        abbreviationsList = parsingProcessor.parse(pathToAbbreviationsFile);
    }

    @Test
    void processMustReturnCorrectResult() {
        String expected = "1. Sebastian Vettel                 |FERRARI                          |1:04.415\n" +
                "2. Daniel Ricciardo                 |RED BULL RACING TAG HEUER        |1:12.013\n" +
                "3. Valtteri Bottas                  |MERCEDES                         |1:12.434\n" +
                "4. Lewis Hamilton                   |MERCEDES                         |1:12.460\n" +
                "5. Stoffel Vandoorne                |MCLAREN RENAULT                  |1:12.463\n" +
                "6. Kimi Raikkonen                   |FERRARI                          |1:12.639\n" +
                "7. Fernando Alonso                  |MCLAREN RENAULT                  |1:12.657\n" +
                "8. Sergey Sirotkin                  |WILLIAMS MERCEDES                |1:12.706\n" +
                "9. Charles Leclerc                  |SAUBER FERRARI                   |1:12.829\n" +
                "10. Sergio Perez                     |FORCE INDIA MERCEDES             |1:12.848\n" +
                "11. Romain Grosjean                  |HAAS FERRARI                     |1:12.930\n" +
                "12. Pierre Gasly                     |SCUDERIA TORO ROSSO HONDA        |1:12.941\n" +
                "13. Carlos Sainz                     |RENAULT                          |1:12.950\n" +
                "14. Esteban Ocon                     |FORCE INDIA MERCEDES             |1:13.028\n" +
                "15. Nico Hulkenberg                  |RENAULT                          |1:13.065\n" +
                "--------------------------------------------------------------------------------\n" +
                "16. Brendon Hartley                  |SCUDERIA TORO ROSSO HONDA        |1:13.179\n" +
                "17. Marcus Ericsson                  |SAUBER FERRARI                   |1:13.265\n" +
                "18. Lance Stroll                     |WILLIAMS MERCEDES                |1:13.323\n" +
                "19. Kevin Magnussen                  |HAAS FERRARI                     |1:13.393";

        String actual = racingRatingProcessor.process(startTimeList, endTimeList, abbreviationsList);

        assertEquals(expected, actual);
    }
}
