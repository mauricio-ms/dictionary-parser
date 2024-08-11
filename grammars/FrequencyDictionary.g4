grammar FrequencyDictionary;

compilationUnit : ((term '\n'? | .+? | '(' | ')' | '\'' | '‘' | '’' | '"' | '“' | '”' | '•' | 'º' | '°' |
    '' | '–' | '|' | '+' | '-' | '/' | '%' | '?' | '!' | ':' | '=' | '$' | '#' ) )* EOF ;

term
    : indexRow
    | frequencyIndexHeader
    | frequencyIndexRow
    | example
    | thematicBoxHeader
    | thematicBoxRow
    ;

indexRow : portugueseWord meaning englishWordList NUMBER ;

frequencyIndexHeader : '' 'Frequency' 'index' ;
frequencyIndexRow : portugueseWord meaning englishWordList MODIFIER? ;

example : '•' .*? ('\n' '\n') ;

thematicBoxHeader : NUMBER '.' WORD+ ;
thematicBoxRow : portugueseWord NUMBER sex? englishWordList ;
sex : 'M' | 'F' | 'MF' | 'C' ;

portugueseWord : word ;
englishWordList : englishWord+ ;
englishWord : MODIFIER? word ('[' portugueseType ']')? MODIFIER? ;
portugueseType : BRAZILIAN_PORTUGUESE | EUROPEAN_PORTUGUESE ;
word : WORD | COMMA ;

meaning
    : 'aj'
    | 'av'
    | 'at'
    | 'cj'
    | 'f'
    | 'i'
    | 'm'
    | 'neut'
    | 'nc'
    | 'nf'
    | 'nm'
    | 'nmf'
    | 'num'
    | 'obj'
    | 'pl'
    | 'prp'
    | 'pn'
    | 'sg'
    | 'v'
    | 'n/av'
    ;

MODIFIER : '(' .*? ')' ;

BRAZILIAN_PORTUGUESE : 'BP' ;
EUROPEAN_PORTUGUESE : 'EP' ;

COMMA : ',' ;
WORD : [a-zA-Z-\u00C0-\u00ff]+ ;

NUMBER : DIGIT+ ;

fragment
    DIGIT: '0'..'9' ;

WS : [ ;\t\r]+ -> skip ;