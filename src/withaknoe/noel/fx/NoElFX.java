package withaknoe.noel.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import withaknoe.noel.core.Primitive;
import withaknoe.noel.core.Vec2;
import withaknoe.noel.lang.NoEl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// ~ https://graphicdesign.stackexchange.com/questions/114575/the-dimensions-of-a-monospaced-font
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

        Layout layout = new Layout(0, 0.1, 0.33, 0.33, 0.34, null);
        double canvasWidth = layout.getCanvasRect().getWidth();
        double canvasHeight = layout.getCanvasRect().getHeight();
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        Color canvasBG = Color.rgb(layout.getRgb().r(), layout.getRgb().g(), layout.getRgb().b());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // CANVAS BACKGROUND
        gc.setFill(canvasBG);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // TEXT BRIGHTNESS for readability
        double brightness = (canvasBG.getRed() + canvasBG.getBlue() + canvasBG.getGreen()) / 3.0;
        gc.setFill(brightness > 0.5 ? Color.BLACK : Color.WHITE);

        // SET FONT IF EXISTS
        if (layout.fira != null) gc.setFont(layout.fira); else gc.setFont(new Font(16));

        Image applicationIcon = new Image(getClass().getResourceAsStream("/resources/images/icon_NoEl.png"));

        stage.getIcons().add(applicationIcon);
        // ----------- some temp ui debug stuff
//        String rgbText = String.format("(r %d, g %d, b %d)",
//                (layout.getRgb().r() / 255),
//                (layout.getRgb().g() / 255),
//                (layout.getRgb().b() / 255));
//
//        Text temp = new Text(rgbText);
//        temp.setFont(gc.getFont());

        // Text width for proper placement
//        double textWidth = temp.getLayoutBounds().getWidth();
//        double rgbTxt_x = (canvas.getWidth() - textWidth) / 2;
//        double rgbTxt_y = 20;
//
//        gc.fillText(rgbText, rgbTxt_x, rgbTxt_y);
        // ---------------------
        // ~ might like the color
        Button button = new Button("Save RGB");
        button.setOnAction(e -> saveColorToFile(canvasBG));

        StackPane root = new StackPane();
        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
        StackPane.setMargin(button, new Insets(0,0,6,0));

        root.getChildren().add(canvas);
        root.getChildren().add(button);

        gc.setStroke(brightness > 0.5 ? Color.BLACK : Color.WHITE);
        gc.setLineWidth(1.0);

        // top letter rectangle border
        gc.strokeLine(layout.getLetterRect().left(), layout.getLetterRect().top(), layout.getLetterRect().right(), layout.getLetterRect().top());
        // bottom
        gc.strokeLine(layout.getLetterRect().left(), layout.getLetterRect().bottom(), layout.getLetterRect().right(), layout.getLetterRect().bottom());
        // left
        gc.strokeLine(layout.getLetterRect().left(), layout.getLetterRect().top(), layout.getLetterRect().left(), layout.getLetterRect().bottom());
        // right
        gc.strokeLine(layout.getLetterRect().right(), layout.getLetterRect().top(), layout.getLetterRect().right(), layout.getLetterRect().bottom());
        gc.setLineWidth(2.0);
        // ~ I like the guidelines

        // TOP / ASCENDER -- ascender is from cap height to ascender
        gc.setStroke(Color.BLUE);
        //gc.setStroke(Color.BLACK);
        gc.strokeLine(layout.getCanvasRect().left(), layout.getLetterRect().top(), layout.getCanvasRect().right(), layout.getLetterRect().top()); // just top line
        // CAPHEIGHT -- ^ but actually
        gc.setLineWidth(1);
        gc.setLineDashes(12.0); // dashed lines so it looks like old school penmanship paper
        gc.setStroke(Color.LIGHTBLUE);
        gc.strokeLine(layout.getLetterRect().left(), layout.getAscenderPx(), layout.getLetterRect().right(), layout.getAscenderPx());
        // BASE
        gc.setLineWidth(2.0);
        gc.setLineDashes(0);
        gc.setStroke(Color.PINK);
        gc.strokeLine(layout.getCanvasRect().left(), layout.getBaselinePx(), layout.getCanvasRect().right(), layout.getBaselinePx());
        // DESCENDER
        gc.setStroke(Color.BLUE);
        gc.strokeLine(layout.getCanvasRect().left(), layout.getDescenderPx(), layout.getCanvasRect().right(), layout.getDescenderPx());

        stage.setTitle("NoEl Renderer - NoElFX");
        Scene scene = new Scene(root, canvasWidth, canvasHeight);

        // TODO Layout layout = LayoutBuilder.build(settings, env);
        //      sink.render(layout, primitives…)
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
        stage.show();
        // FINALLY
        FXRenderSink sink = new FXRenderSink();
        //start 75%x, 25%y, end 50%x, 95%y
        //String source = "line myLine=[0.75, 0.25, 0.5, 0.95].";
        //start 50%x, 10%y, end 75%x, 90%y
        //String source = "line myLine=[0.5, 0.1, 0.75, 0.9].";
        // B
        //String source = "line myLine=[0.05, 0.0, 0.05, 1.0].";
        String source =
                // define B
                "line myLine=[0.05, 0.0, 0.05, 0.66]. " +
                "arc topArc=[0.05, 0.01, 0.05, 0.33, 0.7, -1.2,1]. " +
                "arc bottomArc=[0.05, 0.33, 0.05, 0.66, 0.6, -1.25,1]. " +
                // define i
                "line stem=[0.5, 0.33, 0.45, 0.63]. " +
                "arc i=[0.45, 0.63, 0.53, 0.63, 0.3, 1, 1]. " +
                "line dot=[0.5,0.23,0.501,0.23].";
        NoEl.run(source, sink);
        List<Primitive> ape = sink.primitives();
        Renderer renderer = new Renderer(gc, ape, layout);
        renderer.render();
    }

    // if layout is created with null RGB, it's randomized. This saves the value if button is pushed. aka, if i like it
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











