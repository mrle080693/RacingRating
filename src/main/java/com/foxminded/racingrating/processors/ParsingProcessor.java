package com.foxminded.racingrating.processors;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ParsingProcessor {
    public List<String> process(Path path) {
        List<String> result = new ArrayList<>();

        try (Stream<String> lineStream = Files.lines(path)) {
            lineStream.forEach(result::add);

        } catch (IOException e) {
            throw new FileSystemNotFoundException("Sorry ;( Some troubles with file");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Sorry ;( File path must not be null");
        }

        return result;
    }
}
