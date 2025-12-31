package withaknoe.noel.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static withaknoe.noel.lang.TokenType.*;

//
class Parser {
    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // ~ entry point 'sort of'
    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(DEF))  return function("function");
            if (match(LET))  return varDeclaration();
            if (match(LINE)) return primitiveDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
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
        return assignment();
    }

    private Stmt statement() {
        if (match(FOR))    return forStatement();
        if (match(IF))     return ifStatement();
        if (match(PRINT))  return printStatement();
        if (match(RETURN)) return returnStatement();
        if (match(WHILE))  return whileStatement();
        if (match(LBRACE)) return new Stmt.Block(block());

        return expressionStatement();
    }

    private Stmt forStatement() {
        consume(LPAREN, "Expect '(' after 'for'.");

        Stmt initializer;
        if (match(PERIOD)) {
            initializer = null;
        } else if (match(LET)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }

        Expr condition = null;
        if (!check(PERIOD)) {
            condition = expression();
        }
        consume(PERIOD, "Expect '.' after loop condition.");

        Expr increment = null;
        if (!check(RPAREN)) {
            increment = expression();
        }
        consume(RPAREN, "Expect ')' after for clauses.");
        Stmt body = statement();

        if (increment != null) {
            body = new Stmt.Block(Arrays.asList(body, new Stmt.Expression(increment)));
        }

        if (condition == null) condition = new Expr.Literal(true);
        body = new Stmt.While(condition, body);

        if (initializer != null) {
            body = new Stmt.Block(Arrays.asList(initializer, body));
        }
        return body;
    }

    private Stmt ifStatement() {
        consume(LPAREN, "Expect '(' after 'if'.");
        Expr condition = expression();
        consume(RPAREN, "Expect ')' after if condition.");

        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(ELSE)) {
            elseBranch = statement();
        }

        return new Stmt.If(condition, thenBranch, elseBranch);
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(PERIOD, "Expect '.' after value.");
        return new Stmt.Print(value);
    }

    private Stmt returnStatement() {
        Token keyword = previous();
        Expr value = null;
        if (!check(PERIOD)) {
            value = expression();
        }

        consume(PERIOD, "Expect '.' after return value.");
        return new Stmt.Return(keyword, value);
    }

    private Stmt primitiveDeclaration() {
        List<Expr> properties = new ArrayList<>();
        Token primitiveKeyword = previous();
        String name = consume(IDENTIFIER, "Expect primitive variable name.").lexeme;
        consume(EQUAL, "Expect '=' after primitive name.");
        consume(LBRACKET, "Expect '[' after primitive name.");

        if (!check(RBRACKET)) {
            do {
                properties.add(expression());
            } while (match(COMMA));
        }
        consume(RBRACKET, "Expect ']' after primitive properties.");
        consume(PERIOD, "Expect '.' declaration.");
        return new Stmt.Primitive(primitiveKeyword, name, properties);
    }

    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");

        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

        consume(PERIOD, "Expect '.' after variable declaration.");
        return new Stmt.Let(name, initializer);
    }

    private Stmt whileStatement() {
        consume(LPAREN, "Expect '(' after 'while'.");
        Expr condition = expression();
        consume(RPAREN, "Expect ')' after condition.");
        Stmt body = statement();

        return new Stmt.While(condition, body);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(PERIOD, "Expect '.' after expression.");
        return new Stmt.Expression(expr);
    }

    private Stmt.Function function(String kind) {
        Token name = consume(IDENTIFIER, "Expect " + kind + " name.");
        List<Token> parameters = new ArrayList<>();
        if (!check(RPAREN)) {
            do {
                if (parameters.size() >= 255) {
                    error(peek(), "Can't have more than 255 parameters.");
                }

                parameters.add(consume(IDENTIFIER, "Expect parameter name."));
            } while (match(COMMA));
        }
        consume(RPAREN, "Expect ')' after parameters.");

        consume(LPAREN, "Expect '{' before " + kind + " body.");
        List<Stmt> body = block();
        return new Stmt.Function(name, parameters, body);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();

        while (!check(RBRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(RBRACE, "Expect '}' after block.");
        return statements;
    }

    private Expr assignment() {
        Expr expr = or();

        if (match(EQUAL)) {
            Token equals = previous();
            Expr value = assignment();

            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable)expr).name;
                return new Expr.Assign(name, value);
            }

            error(equals, "Invalid assignment target.");
        }

        return expr;
    }

    private Expr or() {
        Expr expr = and();

        while (match(OR)) {
            Token operator = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, operator, right);
        }

        return expr;
    }

    private Expr and() {
        Expr expr = equality();

        while (match(AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, operator, right);
        }

        return expr;
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

        return call();
    }

    private Expr finishCall(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(RPAREN)) {
            do {
                if (arguments.size() >= 2550) {
                    error(peek(), "Can't have more than 255 arguments.");
                }
                arguments.add(expression());
            } while (match(COMMA));
        }

        Token parent = consume(RPAREN, "Expect ')' after arguments.");
        return new Expr.Call(callee, parent, arguments);
    }

    private Expr call() {
        Expr expr = primary();

        while (true) {
            if (match(LPAREN)) {
                expr = finishCall(expr);
            } else {
                break;
            }
        }

        return expr;
    }

    private Expr primary() {
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(TRUE))
            return new Expr.Literal(true);
        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }
        if (match(IDENTIFIER)) {
            return new Expr.Variable(previous());
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

    // ~ For required tokens - THIS token must be X, or message is the error
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    // ~ consumes > moves to next token > returns previous token (the token that you came in with)
    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF; // ~ last token
    }

    private List<Expr> getProperties() {
        List<Expr> properties = new ArrayList<>();

        do {
            properties.add(expression());
        } while (match(COMMA));

        return properties;
    }

    // ~ feels off because 'peek' is being used to get the current token, but in parsing world it's the NEXT token to be consumed
    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        NoEl.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == PERIOD)
                return;

            switch (peek().type) {
                case LINE:
                case ARC:
                case DEF:
                case LET:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }
}