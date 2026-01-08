package withaknoe.noel.core;

import javafx.scene.shape.ArcType;

public abstract class Primitive {
    public interface Visitor<R> {
        R visitArc(Arc arc);
        R visitDot(Dot dot);
        R visitLine(Line line);
        R visitSweep(Sweep sweep);
        R visitCurve(Curve curve);
    }

    protected String type;
    protected String varName; // passed Variable name from NoEl
    protected Vec2 start;
    protected Vec2 end;

    protected Primitive() {}

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

    // javaFX = strokeArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure)
    // TODO math needed - start/end convert to bounding rectangle?
    public static class Arc extends Primitive {
        enum Direction{CCW, CW}

        private final double angle;
        private final double angleExt;
        private final Vec2 center;
        private final Direction dir; // default direction is clockwise
        private final String type;
        private final double width;
        private final double height;
        public Arc(String name, Vec2 start, Vec2 end, double width, double height, double angle, double angleExt, int clockwise){
            this.type     = "ARC";
            this.varName  = name;
            this.start    = start;
            this.end      = end;
            this.width    = width;
            this.height   = height;
            this.angle    = angle;
            this.angleExt = angleExt;
            this.center   = new Vec2((start.x() + end.x()) / 2, (start.y() + end.y()) / 2);
            this.dir      = clockwise == 1 ? Direction.CW : Direction.CCW;
        }

        @Override
        public String toString() {
            // TODO add arc stuff
            return super.toString();
        }

        public double getAngle() { return this.angle; }
        public double getAngleExt() { return this.angleExt; }
        public String getDirection() {
            return this.dir == Direction.CW ? "clockwise" : "counter-clockwise";
        }
        public double getHeight() { return this.height; }
        public double getWidth() { return this.width; }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visitArc(this); }
    }

    // standard Bezier Curve with 2 control points, WIP
    public static class Curve extends Primitive {
        enum Direction{CCW, CW}
        private final Curve.Direction dir; // default direction is clockwise

        public Curve(String name, Vec2 start, Vec2 end, int clockwise){ // eventually clockwise = boolean
            this.type    = "SWEEP";
            this.varName = name;
            this.start   = start;
            this.end     = end;
            // this.center  = new Vec2((start.x() + end.x()) / 2, (start.y() + end.y()) / 2);
            this.dir = clockwise == 1 ? Curve.Direction.CW : Curve.Direction.CCW;
        }
        @Override
        public String toString() {
            // TODO add Bezier Curve stuff
            return super.toString();
        }

        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visitCurve(this); }
    }

    public static class Dot extends Primitive {
        public Dot() {

        }
        @Override
        public <R> R accept(Visitor<R> visitor) { return visitor.visitDot(this); }
    }

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

    // ~ "STROKE" is reserved for BÉZIER CURVES - Sweep feels the most "hand-writing-esque" for quadratic curves
    public static class Sweep extends Primitive {
        private final double pushStrength;
        private final double pushAt;
        public Sweep(String name, Vec2 start, Vec2 end, double pushAt, double pushStrength){ // eventually clockwise = boolean
            this.type    = "SWEEP";
            this.varName = name;
            this.start   = start;
            this.end     = end;
            this.pushAt  = pushAt;
            this.pushStrength = pushStrength;
        }
        @Override
        public String toString() {
            // TODO add sweep stuff
            return super.toString();
        }

        public double getPushAt()      { return this.pushAt; }
        public double getPushStrength() { return this.pushStrength; }

        @Override
        public <R> R accept(Visitor<R> visitor) {return visitor.visitSweep(this);}
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
