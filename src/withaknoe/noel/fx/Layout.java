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
    private double ascender  = 0.7;
    private double capheight = 0.4;
    private double xheight = 0.25;
    private final double baseline = 0.0;
    private double descender = 0.3;
    // Good light purple 188, 155, 212
    private Rgb rgb = new Rgb(158, 115, 172);
    Font fira = Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Regular.ttf"), 16);
    Layout(double fontHeightPx, double pad, double ascender, double capheight, double xheight, double descender, Rgb rgb) {
        // ~ ternary -> variable = (condition) ? expressionTrue :  expressionFalse;
        this.fontHeightPx        = fontHeightPx == 0 ? 500 : fontHeightPx;
        if (pad != 0) this.padPx = this.fontHeightPx * pad;

        double fontWidthPx = this.fontHeightPx * 0.6;

        this.letterRect          = new Rect(padPx, padPx, fontWidthPx, this.fontHeightPx);
        this.canvasRect          = new Rect(0.0, 0.0, fontWidthPx + (padPx * 2), this.fontHeightPx + (padPx * 2));

        if (ascender != 0.0)     this.ascender = ascender;
        if (capheight != 0.0)    this.capheight = capheight;
        if (xheight != 0.0)      this.xheight = xheight;
        if (descender != 0.0)    this.descender = descender;
        if (rgb != null)         this.rgb = rgb;


            //this.rgb = new Rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
    }
                                                             // temporary default until I get this thing moving
    public double fontHeightPx() { return this.fontHeightPx; }
    public double fontWidthPx()  { return this.fontHeightPx * .66; } // Letter Rect Width
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
                "| ---- BASELINE  " + 0.00 + "%, " + this.getBaselinePx() + "y ---- |\n" +
                "| ---- DESCENDER " + this.descender + "%, " + this.getDescenderPx() + "y ---- |\n";
                // "| Canvas: " +
        StringBuilder strB = new StringBuilder(str);
        strB.append(this.canvasRect);
        return strB.toString();
    }

    public String rgbToString() {
        return "(" + this.rgb.r() + "r, " + this.rgb.g() + "g, " + this.rgb.b() + "b)";
    }

    public double getAscenderPx()  {
        return this.getBaselinePx() - (this.ascender * this.fontHeightPx);
    }

    public double getCapHeightPx()  {
        return this.getBaselinePx() - (this.capheight * this.fontHeightPx);
    }
    public double getXHeightPx()  {
        return this.getBaselinePx() - (this.xheight * this.fontHeightPx);
    }
    public double getBaselinePx()  {
        return (this.ascender * this.fontHeightPx) + padPx;
    }
    public double getDescenderPx()  {
        return Math.abs(this.descender * this.fontHeightPx) + this.getBaselinePx();
    }

    // ---------------------------------------------- //
    /*
         THE FOLLOWING ARE NOEL "WORLD" COORDINATES
                    ie - The Letter Box
              relative to baseline as 0/origin        */
    // ---------------------------------------------- //
    // NoEl-FX Letter Box coordinates -
    public double getNoElFXAscenderPx() {
        double px = this.ascender * this.fontHeightPx;
        return px; // from baseline
    }
    public double getNoElFXCapHeightPx() {
        double px = this.capheight * this.fontHeightPx;
        return px; // from baseline
    }
    public double getNoElFXXHeightPx() {
        double px = this.xheight * this.fontHeightPx;
        return px; // from baseline
    }
    public double getNoElFXBaselinePx() {
        double px = this.baseline * this.fontHeightPx;
        return px; // yes it should return 0.0, but what if I want to offset this later?
    }
    public double getNoElFXDescenderPx() {
        double px = this.descender * this.fontHeightPx;
        return px; // should return negative value, ie -300.0
    }

}
