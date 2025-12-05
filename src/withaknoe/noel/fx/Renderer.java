package withaknoe.noel.fx;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// A stateful visitor that knows how to draw each primitive.
// ~ turns data into pixels
public class Renderer implements Primitive.Visitor<Void> {

    GraphicsContext gc;
    double origin_x;
    double origin_y;
    double fontWidthPx;
    double fontHeightPx;
    HashMap<String, Primitive> primitives;

    Renderer(GraphicsContext gc, HashMap<String, Primitive> primitives, double origin_x, double origin_y,
             double fontWidthPx, double fontHeightPx) {
        this.gc = gc;
        this.origin_x = origin_x;
        this.origin_y = origin_y;
        this.fontWidthPx = fontWidthPx;
        this.fontHeightPx = fontHeightPx;
        this.primitives = primitives;
    }

    @Override
    public Void visitLine(Primitive.Line line){
        double start_x = this.origin_x + (line.getStart().getX() * this.fontWidthPx);
        double start_y = this.origin_y + (line.getStart().getY() * this.fontHeightPx);
        double end_x = this.origin_x + (line.getEnd().getX() * this.fontWidthPx);
        double end_y = this.origin_y + (line.getEnd().getY() * this.fontHeightPx);
        this.gc.strokeLine(start_x, start_y, end_x, end_y);
        return null;
    }

    @Override
    public Void visitArc(Primitive.Arc arc) {
        // gc.strokeArc(x, y, w, h, startAngle, arcExtent, ArcType.OPEN);
        return null;
    }

    public void render(GraphicsContext gc, HashMap<String, Primitive> primitives, double origin_x, double origin_y,
                       double fontWidthPx, double fontHeightPx) {
        //

    }
}
