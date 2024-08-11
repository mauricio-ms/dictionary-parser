package com.lang.trainer.dictionaries.routledge;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class RoutledgePdfParser implements AutoCloseable {
    private static final byte[] NL = "\n".getBytes();

    private static final int VERTICAL_MARGIN = 38;
    private static final int X_MAX = 432;
    private static final int Y_MAX = 663;

    private static final List<String> PREFIX_IGNORED_LINES = List.of(
            // en-pt
            "earshot n:",
            "MEP n",
            "2 (pleasure)",
            "via terrestre.",
            "administrador,-ora, curador,-ora.",
            // pt-en
            "abster-se vr:",
            "de fazer algo to refrain from doing sth.",
            "atrelar vr:",
            "In Portuguese you use double negatives in verbal forms:",
            "não tenho nada.",
            "primeiro,-a-ministro",
            "rola f"
    );

    private final Path pdfPath;
    private final Path outPath;
    private final Path tmpPath;

    RoutledgePdfParser(Path pdfPath, Path outPath) throws IOException {
        this.pdfPath = pdfPath;
        this.outPath = outPath;
        Files.deleteIfExists(outPath);
        Files.createFile(outPath);

        tmpPath = Path.of(outPath + ".tmp");
        Files.createFile(tmpPath);
    }

    public void parse(int startPage, int endPage, Function<String, String> cleanerFn) throws IOException {
        PdfReader reader = new PdfReader(pdfPath.toString());
        float middlePdfPage = ((float) X_MAX / 2) + 2;
        Rectangle leftColumn = new Rectangle(
                0, VERTICAL_MARGIN,
                middlePdfPage, Y_MAX - VERTICAL_MARGIN);
        Rectangle rightColumn = new Rectangle(
                middlePdfPage, VERTICAL_MARGIN,
                X_MAX, Y_MAX - VERTICAL_MARGIN);

        try (FileOutputStream tempOutStream = new FileOutputStream(tmpPath.toString())) {
            for (int i = startPage; i <= endPage; i++) {
                tempOutStream.write(extractText(reader, leftColumn, i).getBytes());
                tempOutStream.write(NL);
                tempOutStream.write(extractText(reader, rightColumn, i).getBytes());
                tempOutStream.write(NL);
            }
        }

        try (
                Stream<String> linesStream = Files.lines(tmpPath);
                FileOutputStream outStream = new FileOutputStream(outPath.toString())
        ) {
            String[] lines = linesStream
                    .filter(l -> PREFIX_IGNORED_LINES.stream().noneMatch(l::startsWith))
                    .toArray(String[]::new);
            for (String line : lines) {
                String cleaned = cleanerFn.apply(line);
                outStream.write(cleaned.getBytes());
                outStream.write("\n".getBytes());
            }
        }
    }

    private String extractText(PdfReader reader, Rectangle region, int pageNumber) throws IOException {
        RenderFilter[] filter = {new RegionTextRenderFilter(region)};
        TextExtractionStrategy strategy = new FilteredTextRenderListener(
                new LocationTextExtractionStrategy(), filter);
        return PdfTextExtractor.getTextFromPage(reader, pageNumber, strategy);
    }

    @Override
    public void close() throws IOException {
        Files.deleteIfExists(tmpPath);
    }

    public static void main(String[] args) throws IOException {
        Path pdfPath = Path.of("file:///home/mauricio/development/projects/lang-trainer/dictionaries/the-routledge-portuguese-bilingual-dictionary-portuguese-english-and-english-portuguese-revised-2014-edition-1nbsped-9780415434348-9780415434331-9780203855393-9781136997266_compress.pdf");
//        parseEnPt(pdfPath);
        parsePtEn(pdfPath);
    }

    private static void parseEnPt(Path pdfPath) throws IOException {
        Path outPath = Path.of("dictionaries/routledge-en-pt.txt");
        try (RoutledgePdfParser parser = new RoutledgePdfParser(pdfPath, outPath)) {
            parser.parse(384, 783, enPtCleanerFn());
            try (RoutledgePdfErrorsFixer fixer = new RoutledgePdfErrorsFixer(
                    outPath,
                    Path.of("dictionaries/routledge-en-pt.error"))) {
                fixer.generateErrorsFile();
                fixer.fixErrors();
            }
        }
    }

    private static Function<String, String> enPtCleanerFn() {
        return l -> includeDot(
                removeParenthesis(
                        normalizeWordSeparator(
                                cleanGroupedChars(l))));
    }

    private static void parsePtEn(Path pdfPath) throws IOException {
        Path outPath = Path.of("dictionaries/routledge-pt-en.txt");
        try (RoutledgePdfParser parser = new RoutledgePdfParser(pdfPath, outPath)) {
            parser.parse(24, 380, ptEnCleanerFn());
            try (RoutledgePdfErrorsFixer fixer = new RoutledgePdfErrorsFixer(
                    outPath,
                    Path.of("dictionaries/routledge-en-pt.error"))) {
                fixer.generateErrorsFile();
                fixer.fixErrors();
            }
        }
    }

    private static Function<String, String> ptEnCleanerFn() {
        return l -> includeSemicolon(normalizeContext(cleanGroupedChars(l)));
    }

    private static String includeSemicolon(String text) {
        return text.replaceAll("bandear vt to sway", "bandear vt to sway;");
    }

    private static String normalizeContext(String text) {
        return text
                .replaceAll("\\[", "(")
                .replaceAll("]", ")");
    }

    private static String includeDot(String text) {
        return text.equals("mentada") ? text + "." : text;
    }

    private static String removeParenthesis(String text) {
        return text
                .replaceAll("\\(c\\)-", "c")
                .replaceAll("\\(c\\)", "c");
    }

    private static String normalizeWordSeparator(String text) {
        return text
                .replaceAll("-a/", "-a,")
                .replaceAll("espião/espiã", "espião,espiã")
                .replaceAll("sumptuoso/", "sumptuoso,")
                .replaceAll("culto,-a. versado,-a", "culto,-a, versado,-a");
    }

    private static String cleanGroupedChars(String text) {
        return text
                .replaceAll("ﬀ", "ff")
                .replaceAll("ﬁ", "fi")
                .replaceAll("ﬂ", "fl")
                .replaceAll("ﬃ", "ffi")
                .replaceAll("ﬄ", "ffl");
    }
}
