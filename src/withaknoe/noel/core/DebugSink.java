package withaknoe.noel.core;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DebugSink implements RenderSink{
    LocalDateTime dt;
    List<Primitive> primitives;
    DecimalFormat df = new DecimalFormat("0.00");

    DebugSink() {
        this.primitives = new ArrayList<>();
        this.dt = LocalDateTime.now();
    }

    public void debug() {
        for (Primitive p : this.primitives) {
            System.out.println("Debug: " + dt.toString());
            System.out.println(p);
        }
    }

    @Override
    public void add(Primitive p) {
        primitives.add(p);
    }

    @Override
    public List<Primitive> primitives() {
        return Collections.unmodifiableList(primitives);
    }

    @Override
    public void clear() {
        if (!(primitives == null)) { this.primitives.clear(); }
    }

    private String f(double d) { return df.format(d);}
}
