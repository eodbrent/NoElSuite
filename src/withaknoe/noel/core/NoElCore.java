package withaknoe.noel.core;

import withaknoe.noel.lang.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// .lang
//      package implements the language : scanner, parser, AST, interpreter, FxVisitor
// .core
//      - hosts REPL, connects components: reads user code, calls .lang
//      - provides sink (RenderSink) interface for .lang, holds primitives
// .fx
//      - draws, implements RenderSink
//      - stores primitives - draws with timer
//  NoElLang_API. RenderSink - bridge between .lang and .fx
public class NoElCore {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Use: noel [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        SinkVisitor sink = new SinkVisitor()
        run(new String(bytes, Charset.defaultCharset()), sink);
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    // REPL
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (; ; ) {
            System.out.println(">>> NoElCore REPL <<<<");
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

}
