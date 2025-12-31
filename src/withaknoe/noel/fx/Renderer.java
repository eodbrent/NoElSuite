package withaknoe.noel.fx;

import javafx.scene.canvas.GraphicsContext;
import withaknoe.noel.core.Primitive;
import java.util.HashMap;
import java.util.List;

// A stateful visitor that knows how to draw each primitive.
// ~ turns data into pixels
public class Renderer implements Primitive.Visitor<Void> {

    GraphicsContext gc;
    double origin_x;
    double origin_y;
    double fontWidthPx;
    double fontHeightPx;
    List<Primitive> primitives;

    Renderer(GraphicsContext gc, List<Primitive> primitives, Layout layout) {
        this.gc = gc;
        this.primitives = primitives;
        this.origin_x = layout.getLetterRect().getX();
        this.origin_y = layout.getLetterRect().getY();
        this.fontWidthPx = layout.fontWidthPx;
        this.fontHeightPx = layout.fontHeightPx;
    }

    @Override
    public Void visitLine(Primitive.Line line){
        double start_x = this.origin_x + (line.getStart().x() * this.fontWidthPx);
        double start_y = this.origin_y + (line.getStart().y() * this.fontHeightPx);
        double end_x = this.origin_x + (line.getEnd().x() * this.fontWidthPx);
        double end_y = this.origin_y + (line.getEnd().y() * this.fontHeightPx);
        this.gc.strokeLine(start_x, start_y, end_x, end_y);
        return null;
    }

    @Override
    public Void visitArc(Primitive.Arc arc) {
        // gc.strokeArc(x, y, w, h, startAngle, arcExtent, ArcType.OPEN);
        return null;
    }


    public void render() {
        for (Primitive p : primitives) {
            p.accept(this);
        }
    }
}
