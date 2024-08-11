package com.lang.trainer.dictionaries.routledge;

import antlr.routledgePtEn.RoutledgePtEnDictionaryBaseVisitor;
import antlr.routledgePtEn.RoutledgePtEnDictionaryParser;
import com.lang.trainer.dictionaries.Dictionary;
import com.lang.trainer.dictionaries.WordsHelper;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;

class RoutledgePtEnDictionary extends RoutledgePtEnDictionaryBaseVisitor<Void> {

    private final Dictionary dictionary;

    public RoutledgePtEnDictionary() {
        dictionary = Dictionary.routledge();
    }

    @Override
    public Void visitEntry(RoutledgePtEnDictionaryParser.EntryContext ctx) {
        System.out.println("visitEntry " + ctx.getText());
        List<String> ptWords = WordsHelper.parseWords(
                ctx.ptWord().word().stream().map(RuleContext::getText));
        List<String> translations = getTranslations(ctx);
        translations.forEach(enWord ->
                dictionary.putTranslationsForWord(enWord, ptWords));
        return super.visitEntry(ctx);
    }

    private List<String> getTranslations(RoutledgePtEnDictionaryParser.EntryContext ctx) {
        return WordsHelper.parseWords(ctx.enWord().stream().map(RuleContext::getText));
    }

    public Dictionary toDictionary() {
        return dictionary;
    }
}
