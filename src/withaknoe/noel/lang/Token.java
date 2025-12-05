package withaknoe.noel.lang;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal; // can be null, num, string etc
    final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() { return type + " " + lexeme + " " + (literal != null ? literal : "");}
}
