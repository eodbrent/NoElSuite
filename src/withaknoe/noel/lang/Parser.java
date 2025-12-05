package withaknoe.noel.lang;

import java.util.List;

import static withaknoe.noel.lang.TokenType.*;

//
class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // ~ entry point 'sort of'
    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    // ~ expression produces value* (t/f #, nil etc) assignments as expressions | nouns
    // ~ statements perform actions - tells interpreter to do something         | verbs
    // ~    for REPL: user -> expression -> REPL evaluates / displays
    // ~              user -> statement -> REPL executes action
    // ~                                                x = 10 (expression)
    // ~                                                x = 10; (statement)
    private Expr expression() {
        return equality();
    }

    // ~ start building/organizing expressions from tokens
    private Expr equality() {
        Expr expr = comparison();
        // ~ waiting for !=  or == so expr (left), operator, right
        while (match(EX_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        // ~ builds comparison type expression 'expr' '>>=<<=' 'term' things start breaking down -> term -> factor -> unary
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        // ~ same deal -, closer and closer to terminals on right side
        while (match(DASH, PLUS)) { // ~ reverse PEMDAS...seriously
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    // ~ division/mult - still breaking down expressions - as it cascades back up, this is evaluated before previous pemdas
    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    // ~ now down to unary, next primary which contains literals
    private Expr unary() {
        if (match(EX, DASH)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE))  return new Expr.Literal(true);
        // if (match(NIL))   return new Expr.Literal(null); // Lox - not sure if nil/null in NoEl

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(LPAREN)) {
            Expr expr = expression();
            consume(RPAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    // ~ consumes and then points to next token
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF; // ~ last token
    }

    private Token peek() {
        return tokens.get(current); // ~ feels off because 'peek' is being used to get the current token, but in parsing world it's the NEXT token to be consumed
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        NoEl.error(token, message);
        return new ParseError();
    }

//    private void synchronize() {
//        advance();
//
//        while (!isAtEnd()) {
//            if (previous().type == SEMICOLON) return;
//
//            switch (peek().type) {
//                case CLASS:
//                case FUN:
//                case VAR:
//                case FOR:
//                case IF:
//                case WHILE:
//                case PRINT:
//                case RETURN:
//                    return;
//            }
//
//            advance();
//        }
    }