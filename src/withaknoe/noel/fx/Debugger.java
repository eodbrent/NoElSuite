package withaknoe.noel.fx;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class Debugger implements Primitive.Visitor<String>{
    LocalDateTime dt;
    HashMap<String, Primitive> primitives;
    DecimalFormat df = new DecimalFormat("0.00");

    Debugger(LocalDateTime dt, HashMap<String, Primitive> primitives) {
        this.dt = dt;
        this.primitives = primitives;
    }

    public void debug() {
        for (HashMap.Entry<String, Primitive> entry : this.primitives.entrySet()) {
            Primitive p = entry.getValue();
            String result = p.accept(this);
            System.out.println("Debug: " + dt.toString());
            System.out.println(result);
        }
    }

    @Override
    public String visitLine(Primitive.Line line){
        String name = line.getVarName();
        double start_x = line.getStart().getX();
        double start_y = line.getStart().getY();
        double end_x = line.getEnd().getX();
        double end_y = line.getEnd().getY();
        return "'" + name + "' [line] --start: x" + start_x + ", y" + start_y + "\n" +
                "            --end:   x" + end_x + ", y" + end_y + "\n======";
    }

    @Override
    public String visitArc(Primitive.Arc arc){
        String name     = arc.getVarName();
        double start_x  = arc.getStart().getX();
        double start_y  = arc.getStart().getY();
        double end_x    = arc.getEnd().getX();
        double end_y    = arc.getEnd().getY();
        double center_x = arc.getCenterNorm().getX();
        double center_y = arc.getCenterNorm().getY();
        String sx = f(start_x); // Force 0.00 so it looks purrtty
        String sy = f(start_y);
        String ex = f(end_x);
        String ey = f(end_y);
        String cx = f(center_x);
        String cy = f(center_y);
        String dir = arc.getDirection();

        System.out.println("raw center: " + center_x + ", " + center_y);
        System.out.println("fmt center: '" + cx + "', '" + cy + "'");
        System.out.println("---");

        return "'" + name + "'  [arc] --start:     x" + sx + ", y" + sy + "\n" +
                "            --end:       x" + ex + ", y" + ey + "\n" +
                "            --center:    x" + cx + ", y" + cy + "\n" +
                "            --direction: " + dir + "\n======";
    }

    private String f(double d) { return df.format(d);}
}
