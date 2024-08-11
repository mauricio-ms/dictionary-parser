package com.lang.trainer.dictionaries;

import java.util.*;
import java.util.stream.Stream;

public final class WordsHelper {

    private WordsHelper() {
    }

    public static List<String> parseWords(Stream<String> wordsStream) {
        LinkedList<String> words = new LinkedList<>();
        List<String> wordParts = new ArrayList<>();
        Runnable addWord = () -> {
            String join = String.join(" ", wordParts).trim();
            if (!join.isEmpty()) {
                words.add(join);
            }
        };

        Iterator<String> wordsIterator = wordsStream.iterator();
        while (wordsIterator.hasNext()) {
            String wordPart = wordsIterator.next();
            if (wordPart.equals("\n")) {
                continue;
            }
            if (wordPart.equals(",")) {
                addWord.run();
                wordParts.clear();
            } else if (wordPart.startsWith("-")) {
                String last = words.getLast();
                String suffix = wordPart.substring(1);
                if (suffix.equals("a") && last.endsWith("o") ||
                        suffix.equals("o") && last.endsWith("a")) {
                    words.add(last.substring(0, last.length() - 1) + suffix);
                } else {
                    Set<Character> newSuffix = new LinkedHashSet<>();
                    int lastSuffixIndex = last.length() - suffix.length();
                    String lastWithoutSuffix = last.substring(lastSuffixIndex);
                    newSuffix.addAll(lastWithoutSuffix
                            .chars()
                            .mapToObj(ch -> (char) ch)
                            .toList());
                    newSuffix.addAll(suffix
                            .chars()
                            .mapToObj(ch -> (char) ch)
                            .toList());
                    words.add(
                            last.substring(0, lastSuffixIndex)
                                    .concat(newSuffix.stream().reduce("", (x, y) -> x + y, String::concat))
                    );
                }
            } else {
                wordParts.add(wordPart);
            }
        }

        addWord.run();
        wordParts.clear();

        return words;
    }
}
