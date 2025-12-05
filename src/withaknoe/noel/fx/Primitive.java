package withaknoe.noel.fx;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import java.util.List;


abstract class Primitive {
    interface Visitor<R> {
        R visitLine(Line line);
        R visitArc(Arc arc);
    }

    protected Point2D startNorm;
    protected Point2D endNorm;
    protected String varName; // passed Variable name from NoEl

    Primitive() {}

    public String getVarName() { return this.varName; }
    public Point2D getStart() { return this.startNorm; }
    public Point2D getEnd() { return this.endNorm; }
    public void setStart() { }
    public void setEnd() { }

    public double[] toDouble(Point2D point) {
        double[] points = {point.getX(), point.getY()};
        return points;
    }

    static class Line extends Primitive {
        Line(String name, double start_x, double start_y, double end_x, double end_y) {
            this.varName   = name;
            this.startNorm = new Point2D(start_x, start_y);
            this.endNorm   = new Point2D(end_x, end_y);
        }

        @Override
        <R> R accept(Visitor<R> visitor) { return visitor.visitLine(this);}
    }

    // Arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length)
    // strokeArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure)
    //  Strokes an Arc using the current stroke paint.
    static class Arc extends Primitive {
        enum Direction{CCW, CW}

        private final Point2D centerNorm;
        private final Direction dir; // default direction is clockwise

        Arc(String name, double start_x, double start_y, double end_x, double end_y, double center_x, double center_y, boolean clockwise){
            this.varName    = name;
            this.startNorm  = new Point2D(start_x, start_y);
            this.endNorm    = new Point2D(end_x, end_y);
            this.centerNorm = new Point2D(center_x, center_y);

            if (clockwise) {this.dir = Direction.CW; } else {this.dir = Direction.CCW;}
        }

        public Point2D getCenterNorm() {
            return this.centerNorm;
        }

        public String getDirection() {
            return this.dir == Direction.CW ? "clockwise" : "counter-clockwise";
        }

        @Override
        <R> R accept(Visitor<R> visitor) {return visitor.visitArc(this);}
    }
    abstract <R> R accept(Visitor<R> visitor);
}
