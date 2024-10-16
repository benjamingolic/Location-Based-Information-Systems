package at.fhooe.sail.ois.map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Random;

/**
 * A JavaFX application that allows users to draw different shapes on a canvas.
 * This class provides methods to draw circles and a custom polygon (Nikolaus House) on a canvas.
 */
public class DrawingTest extends Application {
    private static final String mCANVAS_ID = "drawingTestCanvas";
    private Canvas mCanvas;
    private Random mRand = new Random();

    /**
     * The main method that launches the JavaFX application.
     * @param _argv Command-line arguments.
     */
    public static void main(String[] _argv) {
        launch(_argv);
    }

    /**
     * The main entry point for all JavaFX applications.
     * @param _stage The primary stage for this application.
     */
    @Override
    public void start(Stage _stage) {
        _stage.setTitle("DrawingTest");

        BorderPane root = new BorderPane();
        Scene main = new Scene(root, 640, 480);

        setupCanvasPane(root);
        setupButtonPane(root);

        _stage.setScene(main);
        _stage.show();
    }

    /**
     * Sets up the canvas pane where the shapes will be drawn.
     * @param root The root BorderPane to which the canvas pane is added.
     */
    private void setupCanvasPane(BorderPane root) {
        StackPane canvasPane = new StackPane();
        canvasPane.setMinSize(0, 0);
        mCanvas = new Canvas();
        mCanvas.setId(mCANVAS_ID);
        canvasPane.getChildren().add(mCanvas);
        mCanvas.widthProperty().bind(canvasPane.widthProperty());
        mCanvas.heightProperty().bind(canvasPane.heightProperty());
        canvasPane.setStyle("-fx-background-color: red;");
        root.setCenter(canvasPane);
    }

    /**
     * Sets up the button pane that contains buttons to trigger shape drawing.
     * @param root The root BorderPane to which the button pane is added.
     */
    private void setupButtonPane(BorderPane root) {
        FlowPane buttonPane = new FlowPane();
        buttonPane.setStyle("-fx-background-color: blue; -fx-padding: 5;");

        Button testButton = new Button("Circle Spawn");
        Button testButton2 = new Button("Random Circle Spawn 2");
        Button nikolausButton = new Button("Nikolaus House");

        testButton.setOnAction(event -> drawCircle());
        testButton2.setOnAction(event -> drawRandomCircle());
        nikolausButton.setOnAction(event -> drawNikolausHouse());

        buttonPane.getChildren().addAll(testButton, testButton2, nikolausButton);
        root.setBottom(buttonPane);
    }

    /**
     * Draws a fixed circle at a specified location on the canvas.
     */
    private void drawCircle() {
        GraphicsContext gc = mCanvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillOval(100, 100, 50, 50);
    }

    /**
     * Draws a random circle at a random location on the canvas.
     * Clears the canvas before drawing the new circle.
     */
    private void drawRandomCircle() {
        GraphicsContext gc = mCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
        double x = mRand.nextDouble() * mCanvas.getWidth();
        double y = mRand.nextDouble() * mCanvas.getHeight();
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillOval(x, y, 50, 50);
    }

    /**
     * Draws a custom polygon (Nikolaus House) on the canvas.
     */
    private void drawNikolausHouse() {
        GraphicsContext gc = mCanvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.WHITE);
        double[] x = {10, 40, 40, 25, 10, 10};
        double[] y = {10, 10, 30, 50, 30, 10};
        gc.strokePolygon(x, y, x.length);
    }
}
