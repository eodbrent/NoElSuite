package withaknoe.noel.lang;

import withaknoe.noel.core.RenderSink;

import java.util.List;

// First actual production of NoEl - I'm not reinventing the wheel with some of the capability
//     from the book. Expression AST is essentially the same, as well as the other very basic ones.
//  Structure of the project follows the book as well as the REPL implementation.

/*

Fields, parameters, return types
    Use List
Construction
    Use ArrayList (or whatever implementation)

* **/
public class NoEl {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void run(String source, RenderSink sink) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser( tokens);
        List<Stmt> statements = parser.parse();

        // syntax error from previous
        if (hadError) return;

        //interpreter.interpret(statements);
        Primitivizer fxVisitor = new Primitivizer(sink, interpreter);
        fxVisitor.primitivize(statements);
    }

    static void error(int line, String message) { report(line, "", message); }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    // used by interpreter
//    static void runtimeError(RuntimeError error) {
//        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
//        hadRuntimeError = true;
//    }
    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else  {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }
}