package withaknoe.noel.fx;

public class Rect {
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public Rect(double x,double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return  "        <<< RECT >>>\n" +
                "  origin: " + this.getOrigin()[0] + "x, " + this.getOrigin()[1] + "y. " + this.getWidth() + "w " + this.getHeight() + "h\n" +
                "  _________ top - " + this.top() + "y _________\n" +
                " |                             |\n" +
                " |← l - " + this.left() + "x         r- " + this.right() + "x →|\n" +
                " |             [X]             |\n" +
                " |    center: " + this.getCenter()[0] + "x, " + this.getCenter()[1] +
                " |_______ bottom - " + this.bottom() + "y _______|\n";
    }

    // overkill for now, keeping to see which I use most - or for more user options?
    public double getX()        { return this.x; }
    public double left()        { return this.x; }
    public double right()       { return this.x + this.width; }
    public double getY()        { return this.y; }
    public double top()         { return this.y; }
    public double bottom()      { return this.y + this.height; }
    public double getHeight()   { return this.height; }
    public double getWidth()    { return this.width; }
    public double[] getOrigin() { return new double[]{ this.getX(), this.getY() }; }
    public double centerX()     { return this.getX() + this.width / 2; }
    public double centerY()     { return this.getY() + this.height / 2; }
    public double[] getCenter() { return new double[]{ this.centerX(), this.centerY()}; }
}
