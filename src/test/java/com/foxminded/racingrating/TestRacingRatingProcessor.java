package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.DataFormatException;
import com.foxminded.racingrating.processors.RacingDataParser;
import com.foxminded.racingrating.processors.RacingRatingProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRacingRatingProcessor {
    private Path PATH_TO_START_FILE = Paths.get("src\\test\\resources\\start_file.log");
    private Path PATH_TO_END_FILE = Paths.get("src\\test\\resources\\end_file.log");
    private Path PATH_TO_ABBREVIATIONS_FILE = Paths.get("src\\test\\resources\\abbreviations_file.txt");

    private RacingDataParser racingDataParser = new RacingDataParser();
    private Map<String, LocalTime> startTimeMap;
    private Map<String, LocalTime> endTimeMap;
    private Map<String, String> names;
    private Map<String, String> autos;

    private LocalTime timeForTest = LocalTime.parse("12:04:02.979");

    private RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();

    {
        try {
            startTimeMap = racingDataParser.getTime(PATH_TO_START_FILE);
            endTimeMap = racingDataParser.getTime(PATH_TO_END_FILE);
            names = racingDataParser.getNames(PATH_TO_ABBREVIATIONS_FILE);
            autos = racingDataParser.getAutos(PATH_TO_ABBREVIATIONS_FILE);
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfStartTimeMapIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(null, endTimeMap, names, autos));
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfEndTimeMapIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeMap, null, names, autos));
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfNamesIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, null, autos));
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfAutosIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, null));
    }

    @Test
    void processMustReturnDataFormatExceptionIfStartTimeMapIsEmpty() throws IOException {
        startTimeMap = new HashMap<>();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));
        startTimeMap = racingDataParser.getTime(PATH_TO_START_FILE);
    }

    @Test
    void processMustReturnDataFormatExceptionIfEndTimeMapIsEmpty() throws IOException {
        endTimeMap = new HashMap<>();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));
        endTimeMap = racingDataParser.getTime(PATH_TO_END_FILE);
    }

    @Test
    void processMustReturnDataFormatExceptionIfNamesIsEmpty() throws IOException {
        names = new HashMap<>();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));
        names = racingDataParser.getNames(PATH_TO_ABBREVIATIONS_FILE);
    }

    @Test
    void processMustReturnDataFormatExceptionIfAutosIsEmpty() throws IOException {
        autos = new HashMap<>();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));
        autos = racingDataParser.getAutos(PATH_TO_ABBREVIATIONS_FILE);
    }

    @Test
    void processMustReturnDataFormatExceptionIfStartTimeMapIncludesIncorrectData() throws IOException {
        startTimeMap = new HashMap<>();
        startTimeMap.put("Wrong Key", timeForTest);

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));

        startTimeMap = racingDataParser.getTime(PATH_TO_START_FILE);
    }

    @Test
    void processMustReturnDataFormatExceptionIfEndTimeMapIncludesIncorrectData() throws IOException {
        endTimeMap = new HashMap<>();
        endTimeMap.put("Wrong Key", timeForTest);

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));

        endTimeMap = racingDataParser.getTime(PATH_TO_END_FILE);
    }

    @Test
    void processMustReturnDataFormatExceptionIfNamesIncludesIncorrectData() throws IOException {
        names = new HashMap<>();
        names.put("Wrong Key", "Value");

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));

        names.put("Key", "");
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));

        names = racingDataParser.getNames(PATH_TO_ABBREVIATIONS_FILE);
    }

    @Test
    void processMustReturnDataFormatExceptionIfAutosIncludesIncorrectData() throws IOException {
        autos = new HashMap<>();
        autos.put("Wrong Key", "Value");

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));

        autos.put("Key", "");
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor
                .process(startTimeMap, endTimeMap, names, autos));

        autos = racingDataParser.getNames(PATH_TO_ABBREVIATIONS_FILE);
    }

    @Test
    void processMustReturnCorrectResult() {
        String actual = racingRatingProcessor.process(startTimeMap, endTimeMap, names, autos);
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

        assertEquals(expected, actual);
    }
}
