package withaknoe.noel.core;

public abstract class Primitive {
    public interface Visitor<R> {
        R visitLine(Line line);
        R visitArc(Arc arc);
    }

    protected String type;
    protected String varName; // passed Variable name from NoEl
    protected Vec2 start;
    protected Vec2 end;


    protected Primitive() {}

    public String getType() { return this.type; }
    public String getVarName() { return this.varName; }
    public Vec2 getStart()     { return this.start; }
    public Vec2 getEnd()       { return this.end; }
    public void setStart() { }
    public void setEnd() { }

    // place holder, might need this....
    public double[] toDouble(Vec2 point) {
        double[] points = {point.x(), point.y()};
        return points;
    }

    @Override
    public String toString() {
        return this.varName + " [" + getClass().getSimpleName() + "] " + "start=" + start + " end=" + end;
    }

    private String primitiveType() { return this.type; }

    public static class Line extends Primitive {
        public Line(String name, Vec2 start, Vec2 end) {
            this.type = "LINE";
            this.varName   = name;
            this.start = start;
            this.end   = end;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visitLine(this);}
    }

    // Arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length)
    // strokeArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure)
    //  Strokes an Arc using the current stroke paint.
    public static class Arc extends Primitive {
        enum Direction{CCW, CW}

        private final Vec2 center;
        private final Direction dir; // default direction is clockwise

        public Arc(String name, Vec2 start, Vec2 end, Vec2 center, boolean clockwise){
            this.type    = "ARC";
            this.varName = name;
            this.start   = start;
            this.end     = end;
            this.center  = center;

            if (clockwise) {this.dir = Direction.CW; } else {this.dir = Direction.CCW;}
        }
        public String toString() {
            // TODO add arc stuff
            return super.toString();
        }

        public Vec2 getCenter() {
            return this.center;
        }

        public String getDirection() {
            return this.dir == Direction.CW ? "clockwise" : "counter-clockwise";
        }


        @Override
        public <R> R accept(Visitor<R> visitor) {return visitor.visitArc(this);}
    }
    public abstract <R> R accept(Visitor<R> visitor);
}
