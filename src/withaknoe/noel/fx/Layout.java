package withaknoe.noel.fx;

import javafx.scene.text.Font;


// padPx -:- default will always be 0.1, but if 0 is set, there will not be a rectangle pad.
class Layout {
// settings           - ratio
// coordinates/layout - pixel
    private final Rect canvasRect;
    private final Rect letterRect;
    private final double fontHeightPx;   // letter box Height (canvas height: fontheight + 2*pad)
    private double padPx;
    private double ascender  = 0.35;   // mid   <-  to ascender
    private double baseline  = 0.35;   // baseline  to mid
    private double descender = 0.30;  // descender to baseline
    // Good light purple 188, 155, 212
    private Rgb rgb = new Rgb(158, 115, 172);
    Font fira = Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Regular.ttf"), 16);
    Layout(double fontHeightPx, double pad, double ascender, double baseline, double descender, Rgb rgb) {
        // ~ ternary -> variable = (condition) ? expressionTrue :  expressionFalse;
        this.fontHeightPx        = fontHeightPx == 0 ? 500 : fontHeightPx;
        if (pad != 0) this.padPx = this.fontHeightPx * pad;

        double fontWidthPx = this.fontHeightPx * 0.6;

        this.letterRect          = new Rect(padPx, padPx, fontWidthPx, this.fontHeightPx);
        this.canvasRect          = new Rect(0.0, 0.0, fontWidthPx + (padPx * 2), this.fontHeightPx + (padPx * 2));

        if (ascender != 0.0)       this.ascender = ascender;
        if (baseline != 0.0)       this.baseline = baseline;
        if (descender != 0.0)      this.descender = descender;
        if (rgb != null)           this.rgb = rgb;
            //this.rgb = new Rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
    }
                                                             // temporary default until I get this thing moving
    public double fontHeightPx() { return this.fontHeightPx; }
    public double fontWidthPx()  { return this.fontHeightPx * .66; } // canvasWidth
    public Rect getCanvasRect()  { return this.canvasRect; }
    public Rect getLetterRect()  { return this.letterRect; }
    public Rgb getRgb()          { return this.rgb; }

    // TODO add letter rectangle
    @Override
    public String toString() {
        String str =  "\n       <<<< RENDER LAYOUT >>>>       \n" +
                "| LtrBox: " + this.fontHeightPx + "h, " + this.fontWidthPx() + "w\n" +
                "|    pad: " + this.padPx + "px                    |\n" +
                "|    rgb: " + this.rgbToString() + "        |\n" +
                "| ---- ASCENDER  " + this.ascender + "%, " + this.getAscenderPx() + "y ---- |\n" +
                "| ------------  cHeight ------------ |\n" +
                "| ---- BASELINE  " + this.baseline + "%, " + this.getBaselinePx() + "y ---- |\n" +
                "| ---- DESCENDER " + this.descender + "%, " + this.getDescenderPx() + "y ---- |\n";
                // "| Canvas: " +
        StringBuilder strB = new StringBuilder(str);
        strB.append(this.canvasRect.toString()); // TODO First run will be test of this.
        return strB.toString();
    }

    public String rgbToString() {
        return "(" + this.rgb.r() + "r, " + this.rgb.g() + "g, " + this.rgb.b() + "b)";
    }

    public double getAscenderPx() {
        return (this.ascender * this.fontHeightPx) + padPx;
    }
    public double getBaselinePx() {
        return (this.baseline * this.fontHeightPx) + this.getAscenderPx();
    }
    public double getDescenderPx() {
        return (this.descender * this.fontHeightPx) + this.getBaselinePx();
    }
}
