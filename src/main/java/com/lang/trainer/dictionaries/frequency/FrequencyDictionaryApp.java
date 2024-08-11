package com.lang.trainer.dictionaries.frequency;

import antlr.frequency.FrequencyDictionaryLexer;
import antlr.frequency.FrequencyDictionaryParser;
import com.lang.trainer.dictionaries.Dictionary;
import com.lang.trainer.dictionaries.DictionaryOutputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;

class FrequencyDictionaryApp {

    public static void main(String[] args) {
        try {
            var input = new FileInputStream("dictionaries/frequency.txt");
            var chars = CharStreams.fromStream(input);
            var lexer = new FrequencyDictionaryLexer(chars);
            var tokens = new CommonTokenStream(lexer);
            var parser = new FrequencyDictionaryParser(tokens);
            parser.setBuildParseTree(true);
            var tree = parser.compilationUnit().getRuleContext();
            var visitor = new FrequencyDictionary();
            visitor.visit(tree);
            //System.out.println("TOKENS");
            //tokens.getTokens().forEach(System.out::println);
            Dictionary dictionary = visitor.toDictionary();
            System.out.println(dictionary);
            new DictionaryOutputStream(dictionary).write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
