package withaknoe.noel.fx;

import javafx.scene.text.Font;

class Layout {
// settings           - ratio
// coordinates/layout - pixel
    private final Rect canvasRect;
    private final Rect letterRect;
    int fontHeightPx = 500;   // letter box Height (canvas height: fontheight + 2*pad)
    double fontWidthPx;
    double padPx     = 0;
    double ascender  = 0.33;   // mid   <-  to ascender
    double baseline  = 0.33;   // baseline  to mid
    double descender = 0.34;  // descender to baseline
    // Good light purple 188, 155, 212
    private Rgb rgb = new Rgb(188, 155, 212);
    Font fira = Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Regular.ttf"), 16);
    Layout(int fontHeightPx, double pad, double ascender, double baseline, double descender, Rgb rgb) {
        if (fontHeightPx != 0) { this.fontHeightPx = fontHeightPx; }
        if (pad != 0)          { this.padPx = fontHeightPx * pad; }
        double fontWidthPx = fontHeightPx * 0.66;
        this.fontWidthPx =      fontWidthPx;

        this.letterRect =      new Rect(padPx, padPx, fontWidthPx, fontHeightPx);
        this.canvasRect =      new Rect(0.0, 0.0, fontWidthPx + (padPx * 2), fontHeightPx + (padPx *2));

        if (ascender != 0.0)   { this.ascender = ascender; }
        if (baseline != 0.0)   { this.baseline = baseline; }
        if (descender != 0.0)  { this.descender = descender; }
        if (rgb == null)       { this.rgb = new Rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));}
    }

    public double fontWidthPx() { return this.fontHeightPx * .66; } // canvasWidth
    public Rect getCanvasRect() { return this.canvasRect; }
    public Rect getLetterRect() { return this.letterRect; }
    public Rgb getRgb()         { return this.rgb; }
    // TODO add letter rectangle
    @Override
    public String toString() {
        String str =  "\n       <<<< RENDER LAYOUT >>>>       \n" +
                "| LtrBox: " + this.fontHeightPx + "h, " + this.fontWidthPx() + "w\n" +
                "|    pad: " + this.padPx + "px                    |\n" +
                "|    rgb: " + this.rgbToString() + "        |\n" +
                "| ---- ASCENDER  " + this.ascender + "%, " + this.getAscenderPx() + "y ---- |\n" +
                "| ------------  middle ------------ |\n" +
                "| ---- BASELINE  " + this.baseline + "%, " + this.getBaselinePx() + "y ---- |\n" +
                "| ---- DESCENDER " + this.descender + "%, " + this.getDescenderPx() + "y ---- |\n";
                // "| Canvas: " +
        StringBuilder strB = new StringBuilder(str);
        strB.append(this.canvasRect.toString()); // TODO First run will be test of this.
        return strB.toString();
    }

    private Rect initLetterRect(int fontHeightPx) {
        double x = this.padPx;
        double y = this.padPx;
        double w = this.fontWidthPx();
        double h = fontHeightPx;
        Rect r = new Rect(x, y, w, h);
        return r;
    }

    private Rect initCanvasRect(int fontHeightPx) {
        double w =  this.fontWidthPx() + (this.padPx * 2);
        double h = fontHeightPx + (this.padPx * 2);
        Rect r = new Rect(0.0,0.0, w, h);
        return r;
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
