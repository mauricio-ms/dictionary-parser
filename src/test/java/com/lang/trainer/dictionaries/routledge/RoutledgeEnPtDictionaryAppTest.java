package com.lang.trainer.dictionaries.routledge;

import antlr.routledgeEnPt.RoutledgeEnPtDictionaryLexer;
import antlr.routledgeEnPt.RoutledgeEnPtDictionaryParser;
import com.lang.trainer.dictionaries.Dictionary;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoutledgeEnPtDictionaryAppTest {

    @Test
    void test() throws IOException {
        // 21027
        var expectedDictionary = new ExpectedDictionaryBuilder()
                .put("abductor", "raptor", "raptora")
                .put("aberrant", "aberrante")
                .put("aberration", "aberração")
                .put("abet", "incitar")
                .put("abeyance", "em desuso")
                .put("abhor", "detestar", "abominar")
                .put("abhorrent", "detestável", "repugnante", "abominável")
                .put("abide", "aguentar", "suportar")
                .put("abiding", "constante", "permanente")
                .put("ability", "capacidade")
                .put("abject", "miserável", "abjecto", "abjecta")
                .put("ablaze", "em chamas", "a arder")
                .put("able", "capaz")
                .put("ably", "habilmente", "com competência")
                .put("abnormal", "anormal")
                .put("abnormality", "anormalidade")
                .put("aboard", "a bordo")
                .put("abode", "residência", "domicílio")
                .put("abolish", "abolir")
                .put("abolition", "abolição", "extinção")
                .put("abominable", "abominável", "detestável")
                .put("aborigine", "aborígene")
                .put("abort", "abortar", "fazer abortar")
                .put("abortion", "aborto")
                .put("abortive", "abortado", "abortada")
                .put("abound", "abundar")
                .put("about", "acerca de", "sobre", "a respeito de")
                .put("aback", "para trás")
                .put("abacus", "ábaco")
                .put("abandon", "abandono", "despreocupação")
                .put("abashed", "envergonhado", "envergonhada", "embaraçado", "embaraçada")
                .put("abate", "abater", "abrandar", "amainar")
                .put("abatement", "alívio", "diminuição")
                .put("abattoir", "matadouro")
                .put("abbess", "abadessa", "madre", "superiora")
                .put("abbey", "abadia", "mosteiro")
                .put("abbot", "abade")
                .put("abbreviate", "abreviar", "resumir")
                .put("abbreviation", "abreviatura")
                .put("ABC", "ABC", "abecedário")
                .put("abdicate", "abdicar", "renunciar a")
                .build();

        var input = inputStreamForFirstNLines(88);
        Dictionary dictionary = parseDictionary(input);
        assertEquals(expectedDictionary, dictionary);
    }

    private static class ExpectedDictionaryBuilder {
        private final Dictionary dictionary;

        public ExpectedDictionaryBuilder() {
            this.dictionary = Dictionary.routledge();
        }

        public ExpectedDictionaryBuilder put(String word, String... translations) {
            dictionary.putTranslationsForWord(word, Arrays.asList(translations));
            return this;
        }

        public Dictionary build() {
            return dictionary;
        }
    }

    private static Dictionary parseDictionary(InputStream input) throws IOException {
        var chars = CharStreams.fromStream(input);
        var lexer = new RoutledgeEnPtDictionaryLexer(chars);
        var tokens = new CommonTokenStream(lexer);
        var parser = new RoutledgeEnPtDictionaryParser(tokens);
        parser.setBuildParseTree(true);
        var tree = parser.compilationUnit().getRuleContext();
        var visitor = new RoutledgeEnPtDictionary();
        visitor.visit(tree);
        return visitor.toDictionary();
    }

    private InputStream inputStreamForFirstNLines(int n) throws IOException {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Stream<String> linesStream = Files.lines(Path.of("dictionaries", "routledge-en-pt.txt"))
                        .limit(n)
        ) {
            for (String line : linesStream.toList()) {
                out.write(line.getBytes());
                out.write('\n');
            }
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}