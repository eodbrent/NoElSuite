package withaknoe.noel.lang;

import java.util.List;
import java.util.Map;

public class NoElClass implements NoElCallable {
    final String name;
    final NoElClass superclass;
    private final Map<String, NoElFunction> methods;

    NoElClass(String name, NoElClass superclass, Map<String, NoElFunction> methods) {
        this.superclass = superclass;
        this.name = name;
        this.methods = methods;
    }

    NoElFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        if (superclass != null) {
            return superclass.findMethod(name);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        NoElInstance instance = new NoElInstance(this);
        NoElFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter,arguments);
        }

        return instance;
    }

    @Override
    public int arity() {
        NoElFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

}
