package com.lang.trainer.dictionaries.routledge;

import antlr.routledgeEnPt.RoutledgeEnPtDictionaryLexer;
import antlr.routledgeEnPt.RoutledgeEnPtDictionaryParser;
import antlr.routledgePtEn.RoutledgePtEnDictionaryLexer;
import antlr.routledgePtEn.RoutledgePtEnDictionaryParser;
import com.lang.trainer.dictionaries.Dictionary;
import com.lang.trainer.dictionaries.DictionaryOutputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;

class RoutledgeDictionaryApp {

    public static void main(String[] args) throws IOException {
        Dictionary enPtDictionary = parseEnPt();
        Dictionary ptEnDictionary = parsePtEn();
        System.out.println(enPtDictionary);
        System.out.println(ptEnDictionary);
        Dictionary mergedDictionary = enPtDictionary.merge(ptEnDictionary);
        System.out.println(mergedDictionary);
        new DictionaryOutputStream(mergedDictionary).write();
    }

    private static Dictionary parseEnPt() throws IOException {
        var input = new FileInputStream("dictionaries/routledge-en-pt.txt");
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

    private static Dictionary parsePtEn() throws IOException {
        var input = new FileInputStream("dictionaries/routledge-pt-en.txt");
        var chars = CharStreams.fromStream(input);
        var lexer = new RoutledgePtEnDictionaryLexer(chars);
        var tokens = new CommonTokenStream(lexer);
        var parser = new RoutledgePtEnDictionaryParser(tokens);
        parser.setBuildParseTree(true);
        var tree = parser.compilationUnit().getRuleContext();
        var visitor = new RoutledgePtEnDictionary();
        visitor.visit(tree);
        return visitor.toDictionary();
    }
}
