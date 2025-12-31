package withaknoe.noel.lang;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import withaknoe.noel.core.Primitive;
import withaknoe.noel.core.RenderSink;
import withaknoe.noel.core.Vec2;

// Walks the parsed Stmt tree and sends drawing commands to a RenderSink.
public class Primitivizer implements Stmt.Visitor<Void> {

    private final RenderSink sink;
    private final Interpreter interpreter; // existing interpreter

    public Primitivizer(RenderSink sink, Interpreter interpreter) {
        this.sink = sink;
        this.interpreter = interpreter;
    }

    // Entry point : clear frame and walk all statements.
    void primitivize(List<Stmt> statements) {
        sink.clear();
        try {
            for (Stmt stmt : statements) {
                execute(stmt);
            }
        } catch (RuntimeError error) {
            NoEl.runtimeError(error);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    // ============================
    // Statement visitors
    // ============================
    // Delegate non-drawing to interpreter
    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    @Override
    public Void visitLetStmt(Stmt.Let stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    // evaluate properties, build primitives, call sink.draw()
    @Override
    public Void visitPrimitiveStmt(Stmt.Primitive stmt) {
        if (stmt.properties.size() != 4) {
            throw new RuntimeError(stmt.primitiveKeyword, "Line properties expect 4 doubles.");
        }

        List<Double> doubles = toDouble(stmt.properties);
        Vec2 start = new Vec2(doubles.get(0), doubles.get(1));
        Vec2 end   = new Vec2(doubles.get(2), doubles.get(3));
        Primitive p = new Primitive.Line(stmt.name, start, end);
        System.out.println(p);
        sink.add(p);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        interpreter.interpret(stmt);
        return null;
    }

    //  Draw helpers

    // Line statement example derived from CI methods
    private Object line() {
        // requires variable name
        //          start coords
        //          end   coords
        return null;
    }

    // Stmt/Expr evaluation helpers, most live in Interpreter, these are FxSpecific
    // ~ Syntax Layer -> expr, concrete node -> Expr.Literal, Runtime value -> Object -> Double
    private List<Double> toDouble(List<Expr> values) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMinimumFractionDigits(2);
        List<Double> doubles = new ArrayList<>();
        for (Expr expr : values) {
            // recognize node type
            if (expr instanceof Expr.Literal) {
               Object val = ((Expr.Literal) expr).value;
               double val_dbl = (double) val; // <--- help here
               doubles.add(Double.valueOf(df.format(val_dbl)));
            }
        }
        return doubles;
    }
}
