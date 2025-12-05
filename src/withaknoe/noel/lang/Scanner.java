package withaknoe.noel.lang;

import java.util.*;

import static withaknoe.noel.lang.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",        AND);
        keywords.put("ascender",   ASCENDER);
        keywords.put("base",       BASE);
        keywords.put("descender",  DESCENDER);
        keywords.put("else",       ELSE);
        keywords.put("elseif",     ELSEIF); //TODO needs 2nd thought - elif...
        keywords.put("false",      FALSE);
        keywords.put("for",        FOR);
        keywords.put("identifier", IDENTIFIER);
        keywords.put("if",         IF);
        keywords.put("letter",     LETTER);
        keywords.put("line",       LINE);
        keywords.put("middle",     MIDDLE);
        keywords.put("number",     NUMBER);
        keywords.put("numeral",    NUMERAL);
        keywords.put("or",         OR);
        keywords.put("other",      OTHER);
        //keywords.put("string",     STRING);
        keywords.put("symbol",     SYMBOL);
        keywords.put("return",     RETURN);  // TODO decide how
        keywords.put("true",       TRUE);
        keywords.put("uses",       USES);
        keywords.put("while",      WHILE);
    }

    public Scanner(String source) { this.source = source;}

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch(c) {
            case '(': addToken(LPAREN);   break;
            case ')': addToken(RPAREN);   break;
            case '{': addToken(LBRACE);   break;
            case '}': addToken(RBRACE);   break;
            case '+': addToken(PLUS);     break;  // possibly for concatenation also if strings prove useful
            case '*': addToken(STAR);     break;
            case '/': addToken(SLASH);    break;
            case '.': addToken(DOT);      break;
            case ':': addToken(COLON);    break; // can't remember why I wanted to include this one
            case ',': addToken(COMMA);    break;
            case '>':
                if (match('=')) {
                    addToken(GREATER_EQUAL);
                } else {
                    addToken(GREATER);
                }
                break;
            case '<':
                if (match('=')) {
                    addToken(LESS_EQUAL);
                } else {
                    addToken(LESS);
                }
                break;
            case '!':
                if (match('=')) {
                    addToken(EX_EQUAL);
                } else {
                    addToken(EX);
                }
                break;
            case '=':
                if (match('=')) {
                    addToken(EQUAL_EQUAL);
                } else {
                    addToken(EQUAL);
                }
                break;
            case '-':
                if (match('-')) { // '--' comment
                    // consume until end of line
                    while(peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(DASH);
                }
                break;
            case '"': string();     break;
            case '\n': line++ ;     break;
            case  ' ':
            case '\r':
            case '\t':              break; //ignore white space
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    NoEl.error(line, "Unexpected character: " + c);
                }
                break;
        }
    }

    // included in case I find a purpose for strings.
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') { line++; }
            advance();
        }

        if (isAtEnd()) {
            NoEl.error(line, "Unterminated string.");
            return;
        }

        advance();

        // remove typed quotes
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private boolean match(char expected) {
        if (isAtEnd()) { return false; }
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);

        TokenType type = keywords.get(text);
        if (type == null) type = TokenType.IDENTIFIER;
        addToken(type);
    }
    // i like lox's double integer
    private void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // consume the '.'
            while (isDigit(peek())) advance();
        }
        String value = source.substring(start, current);
        addToken(TokenType.NUMBER, Double.parseDouble(value));
    }

    private boolean isAlpha(char c) { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'; }

    private boolean isAlphaNumeric(char c) { return isAlpha(c) || isDigit(c); }

    private boolean isDigit(char c) { return Character.isDigit(c); }

    private char advance() { return source.charAt(current++); }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAtEnd() { return current >= source.length(); }

    private void addToken(TokenType type) { addToken(type, null); }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
