package withaknoe.noel.lang;

public enum TokenType {
    // single char tokens
    PLUS, DASH, STAR, SLASH,
    PERIOD, COLON, COMMA,
    LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET,

    EX, EQUAL, GREATER, LESS,
    // > two char tokens
    EX_EQUAL, EQUAL_EQUAL, GREATER_EQUAL, LESS_EQUAL,
    CONCAT,

    // literals
    IDENTIFIER,
    NUMBER,
    STRING,

    // KEYWORDS
    DEF, LET, SETTINGS,
    LETTER, SYMBOL, NUMERAL, OTHER, USES,
    ASCENDER, CAPHEIGHT, XHEIGHT, BASE, DESCENDER,
    ARC, LINE, CURVE, DOT, // !TODO more to come
    PRINT, // possibly useful for debug or other assistive capes
    IF, ELSE, ELSEIF, WHILE, FOR,
    TRUE, FALSE, OR, AND,
    RETURN,
    EOF
}
