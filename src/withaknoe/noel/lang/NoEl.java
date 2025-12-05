package withaknoe.noel.lang;

import withaknoe.noel.core.RenderSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// First actual production of NoEl - I'm not reinventing the wheel with some of the capability
//     from the book. Expression AST is essentially the same, as well as the other very basic ones.
//  Structure of the project follows the book as well as the REPL implementation.

public class NoEl {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

//    public static void main(String[] args) throws IOException {
//        if (args.length > 1) {
//            System.out.println("Use: noel [script]");
//            System.exit(64);
//        } else if (args.length == 1) {
//            runFile(args[0]);
//        } else {
//            runPrompt();
//        }
//    }

//    private static void runFile(String path) throws IOException {
//        byte[] bytes = Files.readAllBytes(Paths.get(path));
//        run(new String(bytes, Charset.defaultCharset()));
//        if (hadError) System.exit(65);
//        if (hadRuntimeError) System.exit(70);
//    }
//
//    // REPL
//    private static void runPrompt() throws IOException {
//        InputStreamReader input = new InputStreamReader(System.in);
//        BufferedReader reader = new BufferedReader(input);
//
//        for (; ; ) {
//            System.out.println(">>> ENTERING NoEl.main() <<<<");
//            System.out.print("> ");
//            String line = reader.readLine();
//            if (line == null) break;
//            run(line);
//            hadError = false;
//        }
//    }

//    private static void run(String source) {
//        Scanner scanner = new Scanner(source);
//        List<Token> tokens = scanner.scanTokens();
//        Parser parser = new Parser(tokens);
//        Expr expression = parser.parse();
//
//        // syntax error from previous calls
//        if (hadError) return;
//
//        interpreter.interpret(expression);
//        // print tokens as they are scanned
//        for (Token token : tokens) {
//            System.out.println(token);
//        }
//    }

    public static void run(String source, RenderSink sink) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser( tokens);
        Expr expression = parser.parse();;

        // syntax error from previous
        if (hadError) return;

        interpreter.interpret(expression);

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