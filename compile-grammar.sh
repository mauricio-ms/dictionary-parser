#!/usr/bin/bash
DICTIONARY=$1
case $DICTIONARY in
  frequency)
    DICTIONARY_GRAMMAR="FrequencyDictionary.g4"
    ;;

  routledgeEnPt)
      DICTIONARY_GRAMMAR="RoutledgeEnPtDictionary.g4"
      ;;

  routledgePtEn)
    DICTIONARY_GRAMMAR="RoutledgePtEnDictionary.g4"
    ;;

  *)
    echo -n "Unknown dictionary ${DICTIONARY}; Valid values=(frequency,routledgeEnPt,routledgePtEn)"
esac

rm -f src/main/java/antlr/"$DICTIONARY"/*
cd grammars
java -Xmx500M -cp "/tools/antlr-4.13.1-complete.jar:$CLASSPATH" org.antlr.v4.Tool -visitor -o ../src/main/java/antlr/"$DICTIONARY" -package antlr."$DICTIONARY" $DICTIONARY_GRAMMAR
