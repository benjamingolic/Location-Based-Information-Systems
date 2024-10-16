package at.fhooe.sail.ois.gnss;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * SatView is a custom Pane that implements the IPositionUpdateListener interface to visualize satellite positions.
 * It uses a Canvas to draw satellites and their positions based on vertical and horizontal angles.
 */
public class SatView extends Pane implements IPositionUpdateListener {

    private static final double RADIUS = 150;
    private Canvas mCanvas;
    private NMEAInfo mInfo;

    /**
     * Constructs a SatView and initializes the Canvas for drawing.
     */
    public SatView() {
        initializeCanvas();
        getChildren().add(mCanvas);
    }

    /**
     * Initializes the Canvas and binds its width and height properties to the Pane's properties.
     */
    private void initializeCanvas() {
        mCanvas = new Canvas();
        mCanvas.widthProperty().bind(widthProperty());
        mCanvas.heightProperty().bind(heightProperty());
    }

    public void init() {
        GraphicsContext gc = mCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        drawBaseShapes(gc);
    }

    /**
     * Called when the position is updated. This method updates the internal NMEAInfo object
     * and triggers a redraw of the Canvas on the JavaFX application thread.
     *
     * @param info The new NMEAInfo object containing the updated GNSS data.
     */
    @Override
    public void update(NMEAInfo info) {
        this.mInfo = info;
        Platform.runLater(this::draw);
    }

    /**
     * Draws the satellites and base shapes on the Canvas.
     */
    private void draw() {
        GraphicsContext gc = mCanvas.getGraphicsContext2D();
        clearCanvas(gc);
        drawBaseShapes(gc);
        if (mInfo != null) {
            mInfo.getMSatellites().forEach(satInfo -> drawSatellite(satInfo, gc));
        }
    }

    /**
     * Clears the Canvas.
     *
     * @param gc The GraphicsContext of the Canvas.
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Draws the base shapes (circle and cross lines) on the Canvas.
     *
     * @param gc The GraphicsContext of the Canvas.
     */
    private void drawBaseShapes(GraphicsContext gc) {
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        gc.strokeOval(centerX - RADIUS, centerY - RADIUS, RADIUS * 2, RADIUS * 2);
        gc.strokeLine(centerX - RADIUS - 20, centerY, centerX + RADIUS + 20, centerY);
        gc.strokeLine(centerX, centerY - RADIUS - 20, centerX, centerY + RADIUS + 20);
        gc.fillText("N", centerX + 10, centerY - RADIUS - 20);
        gc.fillText("E", centerX + RADIUS + 20, centerY + 20);
        gc.fillText("S", centerX - 20, centerY + RADIUS + 20);
        gc.fillText("W", centerX - RADIUS - 20, centerY - 10);
        drawElevationCircle(gc, centerX, centerY);
    }

    /**
     * Draws an additional circle for elevation indication.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param centerX The center X coordinate.
     * @param centerY The center Y coordinate.
     */
    private void drawElevationCircle(GraphicsContext gc, double centerX, double centerY) {
        double dist45 = calcDistForVerticalAngle(45);
        gc.strokeOval(centerX - dist45, centerY - dist45, dist45 * 2, dist45 * 2);
    }

    /**
     * Draws a satellite on the Canvas based on its information.
     *
     * @param satInfo The SatelliteInfo object containing the satellite data.
     * @param gc The GraphicsContext of the Canvas.
     */
    private void drawSatellite(SatelliteInfo satInfo, GraphicsContext gc) {
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        double vertDist = calcDistForVerticalAngle(satInfo.getMVerticalAngle());
        double dX = Math.cos(Math.toRadians(satInfo.getMHorizontalAngle() + 90)) * vertDist;
        double dY = Math.sin(Math.toRadians(satInfo.getMHorizontalAngle() + 90)) * vertDist;
        double posX = centerX - dX;
        double posY = centerY - dY;

        setSatelliteColor(gc, satInfo);
        drawSatelliteShape(gc, satInfo, posX, posY);
        drawSatelliteId(gc, satInfo, posX, posY);
    }

    /**
     * Sets the color for the satellite based on its status.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param satInfo The SatelliteInfo object containing the satellite data.
     */
    private void setSatelliteColor(GraphicsContext gc, SatelliteInfo satInfo) {
        if (satInfo.isMIsUsed()) {
            gc.setFill(Color.GREEN);
        } else if (satInfo.getMSNR() <= 0) {
            gc.setFill(Color.RED);
        } else {
            gc.setFill(Color.BLUE);
        }
    }

    /**
     * Draws the shape representing the satellite on the Canvas.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param satInfo The SatelliteInfo object containing the satellite data.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawSatelliteShape(GraphicsContext gc, SatelliteInfo satInfo, double posX, double posY) {
        switch (satInfo.getMType()) {
            case "GPGSV" -> drawGPS(gc, posX, posY); //GP GPGSV
            case "GAGSV" -> drawGalileo(gc, posX, posY); //GA GPGGA
            case "GN" -> drawGNSS(gc, posX, posY);
            case "PQGSV" -> drawBeidou(gc, posX, posY);
            case "GLGSV" -> drawGLONASS(gc, posX, posY);
            default -> drawDefault(gc, posX, posY);
        }
    }

    /**
     * Draws the satellite ID on the Canvas.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param satInfo The SatelliteInfo object containing the satellite data.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawSatelliteId(GraphicsContext gc, SatelliteInfo satInfo, double posX, double posY) {
        gc.setFill(Color.WHITE);
        String id = String.valueOf(satInfo.getMId());
        int idLen = id.length();
        gc.fillText(id, posX - (idLen * 7 - 5), posY + 5);
        gc.setFill(Color.BLACK);
    }

    /**
     * Draws a circular shape representing a GPS satellite.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawGPS(GraphicsContext gc, double posX, double posY) {
        gc.fillOval(posX - 15, posY - 15, 30, 30);
    }

    /**
     * Draws a rounded rectangular shape representing a Galileo satellite.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawGalileo(GraphicsContext gc, double posX, double posY) {
        gc.fillRoundRect(posX - 15, posY - 15, 30, 30, 15, 15);
    }

    /**
     * Draws a square shape representing a GNSS satellite.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawGNSS(GraphicsContext gc, double posX, double posY) {
        gc.fillRect(posX - 15, posY - 15, 30, 30);
    }

    /**
     * Draws a triangular shape representing a GLONASS satellite.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawGLONASS(GraphicsContext gc, double posX, double posY) {
        double x1 = posX;
        double y1 = posY - 20;
        double x2 = posX - 20;
        double y2 = posY + 15;
        double x3 = posX + 20;
        double y3 = y2;
        gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
    }

    /**
     * Draws a hexagonal shape representing a Beidou satellite.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawBeidou(GraphicsContext gc, double posX, double posY) {
        double[] xPoints = new double[6];
        double[] yPoints = new double[6];
        for (int i = 0; i < 6; i++) {
            double rad = 2 * Math.PI / 6 * i;
            xPoints[i] = posX + 15 * Math.cos(rad);
            yPoints[i] = posY + 15 * Math.sin(rad);
        }
        gc.fillPolygon(xPoints, yPoints, 6);
    }

    /**
     * Draws a default triangular shape for unspecified satellite types.
     *
     * @param gc The GraphicsContext of the Canvas.
     * @param posX The X coordinate for the satellite position.
     * @param posY The Y coordinate for the satellite position.
     */
    private void drawDefault(GraphicsContext gc, double posX, double posY) {
        double x1 = posX;
        double y1 = posY - 10;
        double x2 = posX - 20;
        double y2 = posY + 15;
        double x3 = posX + 20;
        double y3 = y1;
        gc.fillPolygon(new double[]{x2, x1, x3}, new double[]{y1, y2, y3}, 3);
    }

    /**
     * Calculates the distance for a given vertical angle.
     *
     * @param verticalAngle The vertical angle.
     * @return The distance calculated based on the vertical angle.
     */
    private double calcDistForVerticalAngle(double verticalAngle) {
        return Math.cos(Math.toRadians(verticalAngle)) * RADIUS;
    }
}