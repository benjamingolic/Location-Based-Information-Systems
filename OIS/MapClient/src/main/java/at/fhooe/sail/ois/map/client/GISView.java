package at.fhooe.sail.ois.map.client;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;

/**
 * A JavaFX application that serves as the view component in a GIS (Geographic Information System) MVC (Model-View-Controller) architecture.
 * This class handles the graphical user interface (GUI) elements and interactions with GIS data.
 */
public class GISView extends Application implements IDataObserver {
    public static final String mCANVAS_ID = "GISCanvas";
    public static final String mOVERLAY_ID = "OverlayCanvas";
    private GISController mController;
    private BufferedImage mImage;
    public Scene mScene;
    private Canvas mOverlayCanvas;
    private TextField mScaleField;
    private ToggleGroup mMenuBGroup;

    /**
     * Starts the GIS application and initializes the GUI components.
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        GISModel model = new GISModel();
        mController = new GISController(model, this);
        model.addMapObserver(this);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: gray;");

        MenuBar menuBar = new MenuBar();

        Menu menuA = new Menu("A");
        menuA.setStyle("-fx-background-color: lightgrey;");
        MenuItem itemA = new MenuItem("Option A");
        itemA.setStyle("-fx-text-fill: black;");
        menuA.getItems().add(itemA);

        Menu menuB = new Menu("B");
        menuB.setStyle("-fx-background-color: lightgrey;");
        RadioMenuItem itemB = new RadioMenuItem("DummyGIS");
        RadioMenuItem itemB2 = new RadioMenuItem("Verwaltungsgrenzen-3857");
        RadioMenuItem itemB3 = new RadioMenuItem("OSM-Server-HGB");
        itemB.setStyle("-fx-text-fill: black;");
        itemB2.setStyle("-fx-text-fill: black;");
        itemB3.setStyle("-fx-text-fill: black;");
        menuB.getItems().addAll(itemB, itemB2, itemB3);

        mMenuBGroup = new ToggleGroup();
        itemB.setToggleGroup(mMenuBGroup);
        itemB2.setToggleGroup(mMenuBGroup);
        itemB3.setToggleGroup(mMenuBGroup);
        itemB.setDisable(true);
        itemB2.setSelected(true);

        menuBar.getMenus().addAll(menuA, menuB);
        root.setTop(menuBar);

        GridPane buttonPane = new GridPane();
        buttonPane.setStyle("-fx-background-color: lightgrey; -fx-padding: 5;");
        buttonPane.setHgap(5);
        buttonPane.setVgap(5);

        Button loadButton = new Button("Load");
        loadButton.setId("Load");
        loadButton.setOnAction(mController.getActionHandler());
        buttonPane.add(loadButton, 0, 1);

        Button ztfButton = new Button("ZTF");
        ztfButton.setId("ZTF");
        ztfButton.setOnAction(mController.getActionHandler());
        buttonPane.add(ztfButton, 1, 1);

        Button zoomInButton = new Button("+");
        zoomInButton.setId("ZoomIn");
        zoomInButton.setOnAction(mController.getActionHandler());
        buttonPane.add(zoomInButton, 3, 1);

        Button zoomOutButton = new Button("-");
        zoomOutButton.setId("ZoomOut");
        zoomOutButton.setOnAction(mController.getActionHandler());
        buttonPane.add(zoomOutButton, 4, 1);

        Button upButton = new Button("N");
        upButton.setId("ScrollUp");
        upButton.setOnAction(mController.getActionHandler());
        buttonPane.add(upButton, 8, 0);

        Button leftButton = new Button("W");
        leftButton.setId("ScrollLeft");
        leftButton.setOnAction(mController.getActionHandler());
        buttonPane.add(leftButton, 7, 1);

        Button downButton = new Button("S");
        downButton.setId("ScrollDown");
        downButton.setOnAction(mController.getActionHandler());
        buttonPane.add(downButton, 8, 2);

        Button rightButton = new Button("E");
        rightButton.setId("ScrollRight");
        rightButton.setOnAction(mController.getActionHandler());
        buttonPane.add(rightButton, 9, 1);

        mScaleField = new TextField("1:");
        mScaleField.setId("Scale");
        mScaleField.setPrefWidth(100);
        mScaleField.setOnKeyPressed(mController.getKeyEventHandler());
        buttonPane.add(mScaleField, 12, 1);

        Button rotateButton = new Button("Rotate");
        rotateButton.setId("Rotate");
        rotateButton.setOnAction(mController.getActionHandler());
        buttonPane.add(rotateButton, 15, 1);

        Button poiButton = new Button("POI");
        poiButton.setId("POI");
        poiButton.setOnAction(mController.getActionHandler());
        buttonPane.add(poiButton, 16, 1);

        Button storeButton = new Button("Store");
        storeButton.setId("Store");
        storeButton.setOnAction(mController.getActionHandler());
        buttonPane.add(storeButton, 17, 1);

        Button stickyButton = new Button("Sticky");
        stickyButton.setId("Sticky");
        stickyButton.setOnAction(mController.getActionHandler());
        buttonPane.add(stickyButton, 18, 1);

        root.setBottom(buttonPane);

        StackPane canvasPane = new StackPane();
        canvasPane.setStyle("-fx-background-color: gray;");
        canvasPane.setMinSize(0, 0);

        Canvas canvas = new Canvas();
        canvas.setId(mCANVAS_ID);
        canvasPane.getChildren().add(canvas);

        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        mOverlayCanvas = new Canvas();
        mOverlayCanvas.setId(mOVERLAY_ID);
        mOverlayCanvas.widthProperty().bind(canvasPane.widthProperty());
        mOverlayCanvas.heightProperty().bind(canvasPane.heightProperty());
        canvasPane.getChildren().add(mOverlayCanvas);

        root.setCenter(canvasPane);

        mScene = new Scene(root, 640, 480);
        mOverlayCanvas.widthProperty().addListener(mController.getChangeHandler());
        mOverlayCanvas.heightProperty().addListener(mController.getChangeHandler());
        mOverlayCanvas.setOnMousePressed(mController.getMouseHandler());
        mOverlayCanvas.setOnMouseReleased(mController.getMouseHandler());
        mOverlayCanvas.setOnMouseDragged(mController.getMouseHandler());
        mOverlayCanvas.setOnScroll(mController.getScrollHandler());

        primaryStage.setTitle("Golić - OIS");
        primaryStage.setScene(mScene);
        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(200);

        primaryStage.show();
    }

    /**
     * Updates the view with a new image provided by the GIS model.
     * @param _img The new image to display.
     */
    @Override
    public void update(BufferedImage _img) {
        mImage = _img;
        repaint();
    }

    /**
     * Updates the scale field with the current scale.
     * @param _scale The new scale to display.
     */
    @Override
    public void updateScale(int _scale) {
        TextField scaleField = (TextField) mScene.lookup("#Scale");
        scaleField.setText("1:" + _scale);
    }

    /**
     * Redraws the image on the canvas using the latest image data.
     */
    public void repaint() {
        WritableImage writable = SwingFXUtils.toFXImage(mImage, null);
        Canvas canvas = (Canvas) mScene.lookup("#" + mCANVAS_ID);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(writable, 0, 0);
    }

    /**
     * Draws a rectangle using XOR mode to show the selection area.
     * @param rect The rectangle to draw.
     */
    public void drawXOR(Rectangle2D rect) {
        GraphicsContext gc = mOverlayCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mOverlayCanvas.getWidth(), mOverlayCanvas.getHeight());
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Clears the XOR drawing mode, effectively erasing the drawn rectangle.
     */
    public void clearXOR() {
        GraphicsContext gc = mOverlayCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mOverlayCanvas.getWidth(), mOverlayCanvas.getHeight());
    }

    /**
     * Translates the image in the specified direction by the specified amounts.
     * @param _dX The amount to translate in the X direction.
     * @param _dY The amount to translate in the Y direction.
     */
    public void translate(double _dX, double _dY) {
        Canvas canvas = (Canvas) mScene.lookup("#" + mCANVAS_ID);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(SwingFXUtils.toFXImage(mImage, null), _dX, _dY);

        gc = mOverlayCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mOverlayCanvas.getWidth(), mOverlayCanvas.getHeight());
    }

    /**
     * Gets the selected server from the toggle group.
     * @return The selected server as a string.
     */
    public String getSelectedServer() {
        return ((RadioMenuItem) mMenuBGroup.getSelectedToggle()).getText();
    }

    /**
     * Gets the visible area of the canvas.
     * @return The visible area as a Rectangle.
     */
    public Rectangle getVisibleArea() {
        Canvas canvas = (Canvas) mScene.lookup("#" + mCANVAS_ID);
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        return new Rectangle(0, 0, (int) width, (int) height);
    }
}