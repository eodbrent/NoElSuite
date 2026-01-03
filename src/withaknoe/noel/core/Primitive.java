package withaknoe.noel.core;

import javafx.scene.shape.ArcType;

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

    // strokeArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure)
    //  Strokes an Arc using the current stroke paint.
    public static class Arc extends Primitive {
        enum Direction{CCW, CW}

        // private final Vec2 center;
        private final Direction dir; // default direction is clockwise
//        private final double width;
//        private final double height;
        private final double pushStrength;
        private final double pushAt;
        public Arc(String name, Vec2 start, Vec2 end, double pushAt, double pushStrength, int clockwise){ // eventually clockwise = boolean
            this.type    = "ARC";
            this.varName = name;
            this.start   = start;
            this.end     = end;
            this.pushAt  = pushAt;
            this.pushStrength = pushStrength;
            // TODO not correct center, as it hasn't been pixilized yet.
            // this.center  = new Vec2((start.x() + end.x()) / 2, (start.y() + end.y()) / 2);
            // TODO clockwise boolean - will require primitivizer rework - right it casts all doubles from params
            this.dir = clockwise == 1 ? Direction.CW : Direction.CCW;
        }
        @Override
        public String toString() {
            // TODO add arc stuff
            return super.toString();
        }

        // public Vec2 getCenter() { return this.center; }
        public String getDirection() {
            return this.dir == Direction.CW ? "clockwise" : "counter-clockwise";
        }

        public double getPushAt()      { return this.pushAt; }
        public double getPushStrength() { return this.pushStrength; }

        @Override
        public <R> R accept(Visitor<R> visitor) {return visitor.visitArc(this);}
    }
    public abstract <R> R accept(Visitor<R> visitor);
}
