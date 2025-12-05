package withaknoe.noel.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

//        Stage (the window)
//          └── Scene (the content area)
//              └── Root Node (like a Group, StackPane, etc.)
//                  └── Canvas (where pixels are drawn)
//                      └── GraphicsContext (the thing that actually draws)
public class NoElFX extends Application {
    @Override
    public void start(Stage stage) {
        // @param fontHeight - default 1000
        // ~ 1 pixel (px) is usually assumed to be 1/96th of an inch
        //   1 point (pt) is assumed to be 1/72nd of an inch.
        //   Therefore, 16px = 12pt.
        // ~ NoElFX pt → SVG pt → Screen via DPI → Perfect scaling

        Screen screen = Screen.getPrimary();
        double output_scaleX = screen.getPrimary().getOutputScaleX();
        double output_scaleY = screen.getPrimary().getOutputScaleY();
        double dpi = screen.getDpi();
        int fontHeightPx = 500;
        int fontheightPt = fontHeightPx * 72 / 96;
        int scalePxPerPt = fontHeightPx / fontheightPt;
        double fontWidthPx = (fontHeightPx * .66);
        double pad = fontHeightPx * .1; // size of vertical pad in percentage
        double canvasHeight = fontHeightPx + pad + pad;
        double canvasWidth = fontWidthPx + pad + pad;
        double topBorder_x1 = 0.0;
        double topBorder_y1 = pad;
        double topBorder_x2 = canvasWidth;
        double topBorder_y2 = pad;
        double bottomBorder_x1 = 0.0;
        double bottomBorder_y1 = canvasHeight - pad;
        double bottomBorder_x2 = canvasWidth;
        double bottomBorder_y2 = canvasHeight - pad;
        double leftBorder_x1 = pad;
        double leftBorder_y1 = 0.0;
        double leftBorder_x2 = pad;
        double leftBorder_y2 = canvasHeight;
        double rightBorder_x1 = canvasWidth - pad;
        double rightBorder_y1 = 0.0;
        double rightBorder_x2 = canvasWidth - pad;
        double rightBorder_y2 = canvasHeight;
        double gbOrigin_x = leftBorder_x1; // glyph box origin x
        double gbOrigin_y = topBorder_y1; // glyph box origin y
        double gbOrigin_w = rightBorder_x1 - leftBorder_x1;
        double gbOrigin_h = bottomBorder_y1 - topBorder_y1;
        double ascender  = 0.33; // mid       to ascender
        double baseline  = 0.66; // baseline  to mid
        double descender = 1.0; //  descender to baseline

        // Good light purple 188, 155, 212
        double r = Math.random();
        double g = Math.random();
        double b = Math.random();
//        bgColor = Color.rgb(
//                (int)(Math.random() * 256),
//                (int)(Math.random() * 256),
//                (int)(Math.random() * 256)
//        );
        // Load Font
        Font fira = Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Regular.ttf"), 16);

        Color canvasBG = Color.rgb((int)(Math.random() * 256), (int)(Math.random() * 256),(int)(Math.random() * 256));
        //Color canvasBG = Color.rgb(236, 226, 186);
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // FILL CANVAS BACKGROUND

        gc.setFill(canvasBG);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // TEXT BRIGHTNESS for readability
        double brightness = (canvasBG.getRed() + canvasBG.getGreen() + canvasBG.getBlue()) / 3.0;
        gc.setFill(brightness > 0.5 ? Color.BLACK : Color.WHITE);
        // SET FONT IF EXISTS
        if (fira != null) gc.setFont(fira); else gc.setFont(new Font(16));

        String rgbText = String.format("(r %d, g %d, b %d)",
                (int)(canvasBG.getRed() * 255),
                (int)(canvasBG.getGreen() * 255),
                (int)(canvasBG.getBlue() * 255)
        );

        Text temp = new Text(rgbText);
        temp.setFont(gc.getFont());

        // Text width for proper placement
        double textWidth = temp.getLayoutBounds().getWidth();
        double rgbTxt_x = (canvas.getWidth() - textWidth) / 2;
        double rgbTxt_y = 20;

        gc.fillText(rgbText, rgbTxt_x, rgbTxt_y);

        Button button = new Button("Save RGB");

        button.setOnAction(e -> saveColorToFile(canvasBG));

        StackPane root = new StackPane();
        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
        StackPane.setMargin(button, new Insets(0,0,6,0));

        root.getChildren().add(canvas);
        root.getChildren().add(button);

        gc.setStroke(brightness > 0.5 ? Color.BLACK : Color.WHITE);
        gc.setLineWidth(1.0);

        gc.strokeLine(topBorder_x1,    topBorder_y1,    topBorder_x2,    topBorder_y2);
        gc.strokeLine(bottomBorder_x1, bottomBorder_y1, bottomBorder_x2, bottomBorder_y2);
        gc.strokeLine(leftBorder_x1,   leftBorder_y1,   leftBorder_x2,   leftBorder_y2);
        gc.strokeLine(rightBorder_x1,  rightBorder_y1,  rightBorder_x2,  rightBorder_y2);
        gc.setLineWidth(2.0);
        // TOP / ASCENDER
        gc.setStroke(Color.BLUE);
        gc.strokeLine(gbOrigin_x, gbOrigin_y, gbOrigin_x + gbOrigin_w, gbOrigin_y);
        // MIDDLE
        gc.setLineWidth(1);
        gc.setLineDashes(12.0);
        gc.setStroke(Color.LIGHTBLUE);
        gc.strokeLine(gbOrigin_x, gbOrigin_y + (ascender * fontHeightPx), gbOrigin_x + gbOrigin_w, gbOrigin_y + (ascender * fontHeightPx));
        // BASE
        gc.setLineWidth(2.0);
        gc.setLineDashes(0);
        gc.setStroke(Color.PINK);
        gc.strokeLine(gbOrigin_x, gbOrigin_y + (baseline * fontHeightPx), gbOrigin_x + gbOrigin_w, gbOrigin_y + (baseline * fontHeightPx));
        // DESCENDER
        gc.setStroke(Color.BLUE);
        gc.strokeLine(gbOrigin_x, gbOrigin_y + (descender * fontHeightPx), gbOrigin_x + gbOrigin_w, gbOrigin_y + (descender * fontHeightPx));

        stage.setTitle("NoEl Renderer - NoElFX");
        Scene scene = new Scene(root, canvasWidth, canvasHeight);


        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);

        // FOR VECTORS
        Pane overlay = new Pane();
        overlay.setMouseTransparent(true);
        overlay.setPickOnBounds(false);
        overlay.setPrefSize(canvasWidth, canvasHeight);

        overlay.prefWidthProperty().bind(canvas.widthProperty());
        overlay.prefHeightProperty().bind(canvas.heightProperty());

        gc.setStroke(Color.BLACK);
        HashMap<String, Primitive> strokes = new HashMap<String, Primitive>();
        Primitive.Line l1 = new Primitive.Line("l1",0.33, 0.00, 0.66, 1.00);
        Primitive.Line l2 = new Primitive.Line("l2", 0.90, 0.10, 0.10, 0.90);
        Primitive.Line l3 = new Primitive.Line("l3", 0.55, 0.26, 0.42, 1.10);
        Primitive.Arc a1 =  new  Primitive.Arc("a1", 0.25, 0.25, 0.75, 0.75, (float)0.50, (float)0.50, true);

        strokes.put("l1", l1);
        strokes.put("l2", l2);
        strokes.put("l3", l3);
        strokes.put("a1", a1);

        Renderer rndrr = new Renderer(gc, strokes, gbOrigin_x, gbOrigin_y, fontWidthPx, fontHeightPx);
        LocalDateTime dt = LocalDateTime.now();
        Debugger dbgr  = new Debugger(dt, strokes);
        dbgr.debug();
        stage.show();
    }

    private void saveColorToFile(Color color) {
        String data = String.format("%d,%d,%d%n",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255)
        );

        try (FileWriter writer = new FileWriter("saved_colors.txt", true)) {
            writer.write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}
