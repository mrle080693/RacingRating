package com.foxminded.racingrating;

import com.foxminded.racingrating.exceptions.DataFormatException;
import com.foxminded.racingrating.parsers.RacingDataParser;
import com.foxminded.racingrating.processors.RacingRatingProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRacingRatingProcessor {
    private static Path TEST_START_LOG_FILE_PATH = Paths.get("src\\test\\resources\\test_start.log");
    private static Path TEST_END_LOG_FILE_PATH = Paths.get("src\\test\\resources\\test_end.log");
    private static Path TEST_ABBREVIATIONS_TXT_FILE_PATH = Paths.get("src\\test\\resources\\test_abbreviations.txt");

    private static RacingDataParser racingDataParser = new RacingDataParser();
    private static Map<String, LocalTime> startTimeMap;
    private static Map<String, LocalTime> endTimeMap;
    private static Map<String, LocalTime> mapForTestingTime = new HashMap<>();
    private static Map<String, String> names;
    private static Map<String, String> autos;
    private Map<String, String> mapForTestingData = new HashMap<>();

    private static LocalTime timeForTest = LocalTime.parse("12:04:02.979");

    private RacingRatingProcessor racingRatingProcessor = new RacingRatingProcessor();

    @BeforeAll
    static void initializer() {
        try {
            startTimeMap = racingDataParser.parseTime(TEST_START_LOG_FILE_PATH);
            endTimeMap = racingDataParser.parseTime(TEST_END_LOG_FILE_PATH);
            names = racingDataParser.parseNames(TEST_ABBREVIATIONS_TXT_FILE_PATH);
            autos = racingDataParser.parseAutos(TEST_ABBREVIATIONS_TXT_FILE_PATH);
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfStartTimeMapIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor.process(null,
                endTimeMap, names, autos));
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfEndTimeMapIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor.process(startTimeMap,
                null, names, autos));
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfNamesIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, null, autos));
    }

    @Test
    void processMustReturnIllegalArgumentExceptionIfAutosIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, names, null));
    }

    @Test
    void processMustReturnDataFormatExceptionIfStartTimeMapIsEmpty() {
        mapForTestingTime.clear();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(mapForTestingTime,
                endTimeMap, names, autos));
    }

    @Test
    void processMustReturnDataFormatExceptionIfEndTimeMapIsEmpty() {
        mapForTestingTime.clear();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                mapForTestingTime, names, autos));
    }

    @Test
    void processMustReturnDataFormatExceptionIfNamesIsEmpty() {
        mapForTestingData.clear();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, mapForTestingData, autos));
    }

    @Test
    void processMustReturnDataFormatExceptionIfAutosIsEmpty() {
        mapForTestingData.clear();
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, names, mapForTestingData));
    }

    @Test
    void processMustReturnDataFormatExceptionIfStartTimeMapIncludesIncorrectData() {
        mapForTestingData.clear();
        mapForTestingTime.put("Wrong Key", timeForTest);

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(mapForTestingTime,
                endTimeMap, names, autos));
    }

    @Test
    void processMustReturnDataFormatExceptionIfEndTimeMapIncludesIncorrectData() {
        mapForTestingTime.clear();
        mapForTestingTime.put("Wrong Key", timeForTest);

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                mapForTestingTime, names, autos));
    }

    @Test
    void processMustReturnDataFormatExceptionIfNamesIncludesIncorrectData() {
        mapForTestingData.clear();
        mapForTestingData.put("Wrong Key", "Value");

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, mapForTestingData, autos));

        mapForTestingData.clear();
        mapForTestingData.put("Key", "");
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, mapForTestingData, autos));
    }

    @Test
    void processMustReturnDataFormatExceptionIfAutosIncludesIncorrectData() {
        mapForTestingData.clear();
        mapForTestingData.put("Wrong Key", "Value");

        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, names, mapForTestingData));

        mapForTestingData.clear();
        mapForTestingData.put("Key", "");
        Assertions.assertThrows(DataFormatException.class, () -> racingRatingProcessor.process(startTimeMap,
                endTimeMap, names, mapForTestingData));
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
