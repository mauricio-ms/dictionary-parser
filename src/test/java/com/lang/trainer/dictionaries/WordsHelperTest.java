package com.lang.trainer.dictionaries;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WordsHelperTest {

    @Test
    void sexTest() {
        List<String> expectedWords = List.of(
                "abjecto",
                "abjecta"
        );
        List<String> words = WordsHelper.parseWords(Stream.of("abjecto", ",", "-a"));
        assertEquals(expectedWords, words);
    }
}