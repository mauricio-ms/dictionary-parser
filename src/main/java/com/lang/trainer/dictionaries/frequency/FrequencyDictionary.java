package com.lang.trainer.dictionaries.frequency;

import antlr.frequency.FrequencyDictionaryBaseVisitor;
import antlr.frequency.FrequencyDictionaryParser;
import com.lang.trainer.dictionaries.Dictionary;
import com.lang.trainer.dictionaries.WordsHelper;
import org.antlr.v4.runtime.RuleContext;

import java.util.*;

class FrequencyDictionary extends FrequencyDictionaryBaseVisitor<Void> {

    private final Dictionary dictionary;

    public FrequencyDictionary() {
        dictionary = Dictionary.frequency();
    }

    @Override
    public Void visitExample(FrequencyDictionaryParser.ExampleContext ctx) {
        return super.visitExample(ctx);
    }

    @Override
    public Void visitFrequencyIndexRow(FrequencyDictionaryParser.FrequencyIndexRowContext ctx) {
        System.out.printf("%s = %s\n", ctx.portugueseWord().getText(), ctx.englishWordList().englishWord()
                .stream()
                .map(RuleContext::getText)
                .toList());

        String translation = ctx.portugueseWord().getText();
        List<String> englishWords = parseWords(ctx.englishWordList());
        dictionary.putTranslationForWords(englishWords, translation);
        return super.visitFrequencyIndexRow(ctx);
    }

    @Override
    public Void visitThematicBoxRow(FrequencyDictionaryParser.ThematicBoxRowContext ctx) {
        System.out.printf("%s %s %s %s\n",
                ctx.portugueseWord().getText(),
                ctx.NUMBER().getText(),
                Optional.ofNullable(ctx.sex())
                        .map(RuleContext::getText)
                        .orElse(""),
                ctx.englishWordList().englishWord()
                        .stream()
                        .map(RuleContext::getText)
                        .toList()
        );

        String translation = ctx.portugueseWord().getText();
        List<String> englishWords = parseWords(ctx.englishWordList());
        dictionary.putTranslationForWords(englishWords, translation);

        return super.visitThematicBoxRow(ctx);
    }

    @Override
    public Void visitIndexRow(FrequencyDictionaryParser.IndexRowContext ctx) {
        System.out.printf("IndexRow: %s %s %s => %s\n",
                ctx.portugueseWord().getText(),
                ctx.NUMBER().getText(),
                ctx.englishWordList().getText(),
                ctx.englishWordList().getText()
        );

        String translation = ctx.portugueseWord().getText();
        List<String> englishWords = parseWords(ctx.englishWordList());
        dictionary.putTranslationForWords(englishWords, translation);
        return super.visitIndexRow(ctx);
    }

    private List<String> parseWords(FrequencyDictionaryParser.EnglishWordListContext ctx) {
        return WordsHelper.parseWords(ctx.englishWord().stream().map(RuleContext::getText));
    }

    public Dictionary toDictionary() {
        return dictionary;
    }
}
