package withaknoe.noel.lang;

public enum TokenType {
    // single char tokens
    PLUS, DASH, STAR, SLASH,
    DOT, COLON, COMMA,
    LPAREN, RPAREN, LBRACE, RBRACE,

    EX, EQUAL, GREATER, LESS,
    // > two char tokens
    EX_EQUAL, EQUAL_EQUAL, GREATER_EQUAL, LESS_EQUAL,

    // literals
    IDENTIFIER,
    NUMBER,
    STRING,

    // KEYWORDS
    LETTER, SYMBOL, NUMERAL, OTHER, USES,
    ASCENDER, MIDDLE, BASE, DESCENDER,
    LINE, // TODO more to come
    IF, ELSE, ELSEIF, WHILE, FOR,
    TRUE, FALSE, OR, AND,
    RETURN,
    EOF
}
