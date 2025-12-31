package withaknoe.noel.core;

import withaknoe.noel.lang.NoEl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

// .lang
//      - implements the language : scanner, parser, AST, interpreter, FxVisitor
// .core
//      - hosts REPL, connects components: reads user code, calls .lang
//      - provides sink (RenderSink) interface for .lang, holds primitives
// .fx
//      - draws, implements RenderSink
//      - stores primitives - draws with timer
//  NoElLang_API. RenderSink - bridge between .lang and .fx

// ~ Each rule in the grammar owns an expression level.
public class NoElCore {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    static RenderSink sink = new DebugSink();

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

        NoEl.run(new String(bytes, Charset.defaultCharset()), sink);
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    // REPL
    // TODO add history[] for command buffer, REPL becomes mini-editor, cycling through lines as if in a text file
    //          cursor - up/down index navigation
    //          temp/draft line - current line that hasn't been entered yet. saved when cursor moves

    // possible re-render intervals, upon Enter, Ctrl+Enter, possibly Render button.
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (; ; ) {
            System.out.println(">>> NoElCore REPL <<<<");
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            NoEl.run(line, sink);
            hadError = false;
        }
    }

}
