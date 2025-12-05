package withaknoe.noel.core;

public interface RenderSink {
    void addLine(String varName, double startX, double startY, double endX, double endY);
    void addArc(String varName);
    void setStroke();
    void config();
    void clear();
}
