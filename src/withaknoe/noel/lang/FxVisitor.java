package withaknoe.noel.lang;

import withaknoe.noel.core.RenderSink;

public class FxVisitor { //implements Expr.Visitor<Object> {

    private final RenderSink sink;
    // private final Environment env; // Necessary for variable lookup Not implemented yet in NoEl

    FxVisitor (RenderSink sink) {
        this.sink = sink;
        // this.env = env
    }

}
