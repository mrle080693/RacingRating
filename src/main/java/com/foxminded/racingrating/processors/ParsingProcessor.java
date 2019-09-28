package com.foxminded.racingrating.processors;

import com.foxminded.racingrating.exceptions.FileIsEmptyException;
import com.foxminded.racingrating.exceptions.WrongPathException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ParsingProcessor {
    public List<String> parse(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Sorry ;( File path must not be null");
        }

        List<String> result = new ArrayList<>();

        try (Stream<String> lineStream = Files.lines(path)) {
            lineStream.forEach(result::add);
            if (result.isEmpty()) {
                throw new FileIsEmptyException();
            }

        } catch (IOException e) {
            throw new WrongPathException();
        }

        return result;
    }
}
