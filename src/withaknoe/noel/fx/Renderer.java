package withaknoe.noel.fx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import withaknoe.noel.core.Primitive;
import java.util.List;

// A stateful visitor that knows how to draw each primitive.
// ~ turns data into pixels
public class Renderer implements Primitive.Visitor<Void> {

    GraphicsContext gc;
    double origin_x;
    double origin_y;
    List<Primitive> primitives;
    Layout layout;

    Renderer(GraphicsContext gc, List<Primitive> primitives, Layout layout) {
        this.gc = gc;
        this.primitives = primitives;
        this.layout = layout;
        this.origin_x = layout.getLetterRect().left();
        this.origin_y = layout.getBaselinePx();
    }

    @Override
    public Void visitLine(Primitive.Line line){
        double start_x = this.origin_x + pX(line.getStart().x(), layout.fontWidthPx());
        double start_y = this.origin_y - pX(line.getStart().y(), layout.fontHeightPx());
        double end_x   = this.origin_x + pX(line.getEnd().x(), layout.fontWidthPx());
        double end_y   = this.origin_y - pX(line.getEnd().y(), layout.fontHeightPx());
        this.gc.setStroke(Color.BLACK);
        //gc.setLineWidth(200.0);
        this.gc.strokeLine(start_x, start_y, end_x, end_y);
        return null;
    }
    //    JavaFX strokeArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure)
    //    x - the X coordinate of the arc. --  y - the Y coordinate of the arc.
    //    w - the width of the arc.        --  h - the height of the arc.
    //    startAngle - the starting angle of the arc in degrees. arcExtent - arcExtent the angular extent of the arc in degrees.
    //    closure - (Round, Chord, Open).
//    @Override
//    public Void visitArc(Primitive.Arc arc) {
//        double x = this.origin_x + pX(arc.getStart().x(), layout.fontWidthPx());
//        double y = this.origin_y + pX(arc.getStart().y(), layout.fontHeightPx());
//        double w = pX(arc.getWidth(), layout.fontWidthPx());
//        double h = pX(arc.getHeight(), layout.fontHeightPx());
//        double angle = arc.getAngle();
//        double aExtent = arc.getAngExt();
//        gc.strokeRect(x,y,w,h);
//        gc.strokeArc(x,y,w,h,angle,aExtent, ArcType.OPEN);
//        return null;
//    }

    // TODO Change this Arc to curve, or other term. use basic arc above for easy rounding of arcs without control pts
    @Override
    public Void visitArc(Primitive.Arc arc) {
        double start_x = this.origin_x + pX(arc.getStart().x(), layout.fontWidthPx());
        double start_y = this.origin_y + pX(arc.getStart().y(), layout.fontHeightPx());
        double end_x = this.origin_x + pX(arc.getEnd().x(), layout.fontWidthPx());
        double end_y = this.origin_y + pX(arc.getEnd().y(), layout.fontHeightPx());
        double dx = end_x - start_x;
        double dy = end_y - start_y;
        double base_x = start_x + (dx * arc.getPushAt());
        double base_y = start_y + (dy * arc.getPushAt());

        double length = Math.hypot(dx, dy);
        double pushDistance = length * arc.getPushStrength();
        double perp_x = -dy / length;  // perpendicular
        double perp_y = dx / length;   // perpendicular
        // pushTo should be entered as a sort of "how much" to push - scales with line length
        // controlpt = base point + chord pushat_x/y * (strength * chordLength)
        double pushTo_x = base_x + perp_x * pushDistance;
        double pushTo_y = base_y + perp_y * pushDistance;
        gc.beginPath();
        gc.moveTo(start_x, start_y);
        // ~
        gc.quadraticCurveTo(pushTo_x, pushTo_y, end_x, end_y);
        //gc.setLineWidth(60.0);
        gc.stroke();
        return null;
    }
    public void render() {
        for (Primitive p : primitives) {
            p.accept(this);
        }
    }

    private double pX(double nonPx, double Px) {
        return nonPx * Px;
    }
}


/* ~
The relationship between character width and font height is not standardized across all fonts, but a common
  approximation used in web design is that the average character width is roughly proportional to the font size divided
  by a character constant. One suggested approach uses a character constant of approximately 1.618 (the golden ratio),
  leading to an average character width of font size divided by 1.618.
 For instance, with a 14px font size, the average character width would be approximately 8.65px (14 / 1.618), and a line
   width of 50 characters would be about 433px (50 × 8.65)
*/