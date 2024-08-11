package com.lang.trainer.dictionaries.routledge;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class RoutledgePdfErrorsFixer implements AutoCloseable {
    private final Path parsedPdfPath;
    private final Path errorsPath;

    RoutledgePdfErrorsFixer(Path parsedPdfPath, Path errorsPath) {
        this.parsedPdfPath = parsedPdfPath;
        this.errorsPath = errorsPath;
    }

    public void generateErrorsFile() throws IOException {
        Files.deleteIfExists(errorsPath);
        Files.createFile(errorsPath);
        try (Stream<String> linesStream = Files.lines(parsedPdfPath)) {
            List<String> errors = new ArrayList<>();
            boolean consumed = false;
            List<String> lines = linesStream.toList();
            for (int i = 0; i < lines.size(); i++) {
                String l = lines.get(i);
                if (consumed) {
                    consumed = !l.endsWith(".");
                    continue;
                }
                if (isError(l)) {
                    int semicolonIndex = l.indexOf('.');
                    while (semicolonIndex == -1) {
                        errors.add((i + 1) + "=" + l);
                        l = lines.get(++i);
                        semicolonIndex = l.indexOf('.');
                    }
                    errors.add((i + 1) + "=" + l);
                }
                consumed = !l.endsWith(".");
            }

            try (FileOutputStream outStream = new FileOutputStream(errorsPath.toString())) {
                for (String l : errors) {
                    outStream.write(l.getBytes());
                    outStream.write('\n');
                }
            }
        }
    }

    private boolean isError(String l) {
        if (l.length() > 1 && l.indexOf(' ') == -1) {
            return true;
        }
        int dotIndexOf = l.indexOf('.');
        if (dotIndexOf != -1 &&
                !l.endsWith(".") &&
                l.substring(dotIndexOf).indexOf(' ') == -1) {
            return true;
        }
        int semiColonIndexOf = l.indexOf(';');
        if (semiColonIndexOf != -1 && l.substring(0, semiColonIndexOf).indexOf(' ') == -1) {
            return true;
        }
        return false;
    }

    public void fixErrors() throws IOException {
        try (Stream<String> parsedStream = Files.lines(parsedPdfPath)) {
            String[] parsedLines = parsedStream.toArray(String[]::new);
            Files.deleteIfExists(parsedPdfPath);
            Files.createFile(parsedPdfPath);
            try (FileOutputStream outStream = new FileOutputStream(parsedPdfPath.toString())) {
                BiFunction<Integer, String, String> cleanerFn = cleanErrorsFn();
                for (int i = 0; i < parsedLines.length; i++) {
                    String line = parsedLines[i];
                    String cleaned = cleanerFn.apply(i + 1, line);
                    if (cleaned.isEmpty()) {
                        continue;
                    }
                    outStream.write(cleaned.getBytes());
                    outStream.write("\n".getBytes());
                }
            }
        }
    }

    private BiFunction<Integer, String, String> cleanErrorsFn() throws IOException {
        try (Stream<String> linesStream = Files.lines(errorsPath)) {
            Map<Integer, String> errors = linesStream
                    .map(l -> l.split("="))
                    .collect(Collectors.toMap(
                            p -> Integer.parseInt(p[0]),
                            p -> p[1]));
            return (i, l) -> errors.containsKey(i) ? "" : l;
        }
    }

    @Override
    public void close() throws IOException {
        Files.deleteIfExists(errorsPath);
    }

    public static void main(String[] args) throws IOException {
        try (RoutledgePdfErrorsFixer fixer = new RoutledgePdfErrorsFixer(
                Path.of("dictionaries/routledge-en-pt.txt"),
                Path.of("dictionaries/routledge-en-pt.error"))) {
            fixer.generateErrorsFile();
            fixer.fixErrors();
        }
    }
}
