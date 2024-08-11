package com.lang.trainer.dictionaries.routledge;

import antlr.routledgeEnPt.RoutledgeEnPtDictionaryBaseVisitor;
import antlr.routledgeEnPt.RoutledgeEnPtDictionaryParser;
import com.lang.trainer.dictionaries.Dictionary;
import com.lang.trainer.dictionaries.WordsHelper;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class RoutledgeEnPtDictionary extends RoutledgeEnPtDictionaryBaseVisitor<Void> {

    private final Dictionary dictionary;

    public RoutledgeEnPtDictionary() {
        dictionary = Dictionary.routledge();
    }

    @Override
    public Void visitEntry(RoutledgeEnPtDictionaryParser.EntryContext ctx) {
        System.out.println("visitEntry " + ctx.getText());
        String enWord = ctx.enWord().getText();
        ctx.ptWord().stream().map(RuleContext::getText).toList();
        List<String> translations = getTranslations(ctx);
//        System.out.println(translations);
        dictionary.putTranslationsForWord(enWord, translations);
        return super.visitEntry(ctx);
    }

    private List<String> getTranslations(RoutledgeEnPtDictionaryParser.EntryContext ctx) {
        return WordsHelper.parseWords(ctx.ptWord().stream()
                .flatMap(w -> Optional.ofNullable(w.SEX())
                        .map(s -> Stream.of(w.word().getText(), ","))
                        .orElseGet(() -> Stream.of(w.word().getText()))));
    }

    public Dictionary toDictionary() {
        return dictionary;
    }
}
