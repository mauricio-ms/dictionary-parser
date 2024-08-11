grammar RoutledgePtEnDictionary;

compilationUnit : ((term '\n'? | .+? | '(' | ')' | '\'' | '‘' | '’' | '"' | '“' | '”' | '•' | 'º' | '°' |
    '' | '–' | '|' | '+' | '-' | '/' | '%' | '?' | '!' | ':' | '=' | '$' | '#' ) )* EOF ;

term
    : enWord 'pref' .*? '.' '\n'
    | entry
    | .*? '.'
    ;

entry
    : ptWord context? (type | SEX)? context? nasality? enWord+ context? examples
    ;

type
    : 'adj' 'adv'?
    | 'adj' 'pp'?
    | 'adv'
    | 'npr' 'adj'?
    | 'n' 'adj'?
    | 'pp' 'adj'?
    | 'prep'
    | 'vr'
    | 'vt' ('/' 'vi')?
    | 'vt' (',' 'vi')?
    | 'vt' ('/' 'vr')?
    | 'vt' (',' 'vr')?
    | 'vi'
    ;

context : '(' .*? ')' ;

nasality : ':' .*? '~' ;

ptWord : context? word+ SEX? context? ;
enWord : word ;
word : WORD | COMMA | '\n' ;

examples
    : ';' .*? '.' '\n'
    | NUMBER .*? '.' '\n'
    | '.' '\n'
    ;

NUMBER : DIGIT+ ;
fragment
    DIGIT: '0'..'9' ;

SEX : 'm' (',' 'f')? | 'f' ;
COMMA : ',' ;
WORD : [a-zA-Z-’\u00C0-\u00ff]+ ;

IGNORE : [.♦~…®©〃£&œ@*˜ª´¼0-9a-zA-Z-\u00C0-\u00ff] ;
WS : [ \t\r]+ -> skip ;