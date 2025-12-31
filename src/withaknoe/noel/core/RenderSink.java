package withaknoe.noel.core;

import java.util.List;

public interface RenderSink {
    List<Primitive> primitives();
    void add(Primitive p);
    //void addLine(String varName, double startX, double startY, double endX, double endY);
    //void addArc(String varName);
    //void setStroke();
    //void config();
    void clear();
}
