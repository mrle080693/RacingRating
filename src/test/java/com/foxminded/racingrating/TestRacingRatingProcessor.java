package com.foxminded.racingrating;

import com.foxminded.racingrating.processors.RacingRatingProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRacingRatingProcessor {
    private RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();

    @Test
    void processMustReturnIllegalArgumentExceptionIfFilePathIsNull() {
        Path start = Paths.get("start.log");
        Path end = Paths.get("end.log");
        Path abbreviations = Paths.get("abbreviations.txt");

        // Only first is null
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(null, end, abbreviations);
        });
        // Only second is null
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(start, null, abbreviations);
        });
        // Only third is null
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(start, end, null);
        });
        // All
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(null, null, null);
        });
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfFilePathIsNotCorrect() {
        // Wrong path of start.log
        Path start = Paths.get("wrong.log");
        Path end = Paths.get("end.log");
        Path abbreviations = Paths.get("abbreviations.txt");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(start, end, abbreviations);
        });
        // Wrong path of end.log
        Path start1 = Paths.get("start.log");
        Path end1 = Paths.get("wrong.log");
        Path abbreviations1 = Paths.get("abbreviations.txt");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(start1, end1, abbreviations1);
        });
        // Wrong path of abbreviations.txt
        Path start2 = Paths.get("start.log");
        Path end2 = Paths.get("end.log");
        Path abbreviations2 = Paths.get("wrong.txt");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(start2, end2, abbreviations2);
        });
        // All are wrong
        Path start3 = Paths.get("wrong.log");
        Path end3 = Paths.get("wrong_too.log");
        Path abbreviations3 = Paths.get("wrong_way.txt");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            racingRatingProcessor.process(start3, end3, abbreviations3);
        });
    }

    @Test
    void processMustReturnCorrectResult() {
        Path start = Paths.get("start.log");
        Path end = Paths.get("end.log");
        Path abbreviations = Paths.get("abbreviations.txt");

        String expected =
                "1. Sebastian Vettel                 |FERRARI                          |1:04.415\n" +
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

        String actual = racingRatingProcessor.process(start, end, abbreviations);

        assertEquals(expected, actual);
    }
}
