package withaknoe.noel.lang;

import java.util.List;

class NoElFunction implements NoElCallable {
    private final Stmt.Function declaration;
    private final Environment closure;

    private final boolean isInitializer;

    NoElFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
        this.isInitializer = isInitializer;
        this.declaration = declaration;
        this.closure = closure;
    }

    NoElFunction bind(NoElInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);

        return new NoElFunction(declaration, environment, isInitializer);
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            if (isInitializer) return closure.getAt(0, "this");
            return returnValue.value;
        }

        if (isInitializer) return closure.getAt(0,"this");
        return null;
    }
}
