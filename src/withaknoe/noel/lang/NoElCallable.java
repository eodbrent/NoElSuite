package withaknoe.noel.lang;

import java.util.List;

interface NoElCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
