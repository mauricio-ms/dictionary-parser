package com.lang.trainer.dictionaries;

import java.nio.file.Path;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Dictionary extends TreeMap<String, SortedSet<String>> {
    enum Type {
        FREQUENCY, ROUTLEDGE;

        public Path filePath() {
            return Path.of("%s.json"
                    .formatted(name().toLowerCase()));
        }

        public Path prettyPrintFilePath() {
            return Path.of("%s-pretty-print.json"
                    .formatted(name().toLowerCase()));
        }
    }

    private final Type type;

    public static Dictionary frequency() {
        return new Dictionary(Type.FREQUENCY);
    }

    public static Dictionary routledge() {
        return new Dictionary(Type.ROUTLEDGE);
    }

    private Dictionary(Type type) {
        this.type = type;
    }

    public Dictionary merge(Dictionary other) {
        if (other.type != type) {
            throw new IllegalArgumentException("Incompatible dictionary to be merged.");
        }
        Dictionary merged = new Dictionary(type);
        merged.putAll(this);
        merged.putAll(other);
        return merged;
    }

    public void putTranslationForWords(List<String> words, String translation) {
        words
                .forEach(w ->
                        compute(w, (k, v) -> {
                            if (v == null) {
                                v = new TreeSet<>();
                            }
                            v.add(translation);
                            return v;
                        }));
    }

    public void putTranslationsForWord(String word, List<String> translations) {
        compute(word, (k, v) -> {
            if (v == null) {
                v = new TreeSet<>(translations);
            }
            v.addAll(translations);
            return v;
        });
    }

    public Path filePath() {
        return type.filePath();
    }

    public Path prettyPrintFilePath() {
        return type.prettyPrintFilePath();
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "type=" + type +
                ", values=\n\t" + entrySet()
                .stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining("\n\t")) +
                "\n, size=" + size() +
                '}';
    }
}
