grammar MongoSQL;

// PARSER RULES

statement
    : selectStatement EOF
    ;

selectStatement
    : selectPart fromPart wherePart? limitPart? offsetPart?
    ;

selectPart
    : SELECT (ASTERISK | (identifier ',')* identifier)
    ;

fromPart
    : FROM identifier
    ;

wherePart
    : WHERE (expression AND)* expression
    ;

limitPart
    : LIMIT number
    ;

offsetPart
    : OFFSET number
    ;

expression
    : identifier operator value
    ;

identifier
    : STRING_LITERAL
    ;

number
    : NUMBER_LITERAL
    ;

value
    : STRING_LITERAL
    | NUMBER_LITERAL
    ;

operator
    : EQ
    | NE
    | LT
    | GT
    ;

// LEXER RULES

SELECT
    : S E L E C T
    ;

FROM
    : F R O M
    ;

OFFSET
    : O F F S E T
    ;

LIMIT
    : L I M I T
    ;

WHERE
    : W H E R E
    ;

AND
    : A N D
    ;

EQ
    : '='
    ;

NE
    : '<>'
    ;

LT
    : '<'
    ;

GT
    : '>'
    ;

ASTERISK
    : '*'
    ;

NUMBER_LITERAL
    : '0'
    | [1-9][0-9]*
    ;

STRING_LITERAL
    : [_a-zA-Z][_a-zA-Z0-9]*
    | '\'' (~'\'')* '\''
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];