package at.fhooe.sail.ois.gnss;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * DeviationView is a custom Pane that implements the IPositionUpdateListener interface to visualize
 * the deviation of GNSS data points from an initial position. It displays these points within a circular area,
 * showing the path taken by drawing lines and dots.
 */
public class DeviationView extends Pane implements IPositionUpdateListener {

    private static final double OUTER_RADIUS = 150.0;
    private static final double CENTER_X = 200.0;
    private static final double CENTER_Y = 200.0;

    private List<Double> mLatitudes = new ArrayList<>();
    private List<Double> mLongitudes = new ArrayList<>();
    private Circle mOuterCircle;

    /**
     * Constructor for DeviationView. Initializes the view by setting up the outer circle.
     */
    public DeviationView() {
        initializeView();
    }

    /**
     * Initializes the view by creating and configuring the outer circle and adding it to the Pane.
     */
    private void initializeView() {
        mOuterCircle = new Circle(CENTER_X, CENTER_Y, OUTER_RADIUS);
        mOuterCircle.setFill(null);
        mOuterCircle.setStroke(Color.BLACK);
        getChildren().add(mOuterCircle);
    }

    /**
     * Called when the position is updated. This method updates the lists of latitudes and longitudes
     * and triggers a redraw of the view on the JavaFX application thread.
     *
     * @param info The new NMEAInfo object containing the updated GNSS data.
     */
    @Override
    public void update(NMEAInfo info) {
        Platform.runLater(() -> {
            mLatitudes.add(info.getMLat());
            mLongitudes.add(info.getMLon());
            redraw();
        });
    }

    /**
     * Redraws the view by clearing the current children, re-adding the outer circle, and drawing the lines and dots
     * representing the path taken.
     */
    private void redraw() {
        getChildren().clear();
        getChildren().add(mOuterCircle);

        double maxDeviation = calculateMaxDeviation();
        double scaleFactor = maxDeviation > 0 ? OUTER_RADIUS / maxDeviation : 0;

        for (int i = 1; i < mLatitudes.size(); i++) {
            drawLineAndDot(i, scaleFactor);
        }
        drawLastDot(scaleFactor);
    }

    /**
     * Draws a line and a dot for the given index, scaled appropriately.
     *
     * @param index The index of the current position in the list.
     * @param scaleFactor The factor by which to scale the deviation for drawing.
     */
    private void drawLineAndDot(int index, double scaleFactor) {
        double x1 = CENTER_X + (mLongitudes.get(index - 1) - mLongitudes.get(0)) * scaleFactor;
        double y1 = CENTER_Y - (mLatitudes.get(index - 1) - mLatitudes.get(0)) * scaleFactor;
        double x2 = CENTER_X + (mLongitudes.get(index) - mLongitudes.get(0)) * scaleFactor;
        double y2 = CENTER_Y - (mLatitudes.get(index) - mLatitudes.get(0)) * scaleFactor;

        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(index == mLatitudes.size() - 1 ? Color.RED : Color.BLACK);
        getChildren().add(line);

        Circle dot = new Circle(x1, y1, 2, index == mLatitudes.size() - 1 ? Color.RED : Color.BLACK);
        getChildren().add(dot);
    }

    /**
     * Draws the last dot in the series, which is typically the current position.
     *
     * @param scaleFactor The factor by which to scale the deviation for drawing.
     */
    private void drawLastDot(double scaleFactor) {
        if (!mLatitudes.isEmpty()) {
            double lastX = CENTER_X + (mLongitudes.get(mLongitudes.size() - 1) - mLongitudes.get(0)) * scaleFactor;
            double lastY = CENTER_Y - (mLatitudes.get(mLatitudes.size() - 1) - mLatitudes.get(0)) * scaleFactor;
            Circle lastDot = new Circle(lastX, lastY, 2, Color.RED);
            getChildren().add(lastDot);
        }
    }

    /**
     * Calculates the maximum deviation of the latitude and longitude points from the initial position.
     *
     * @return The maximum deviation calculated as the Euclidean distance from the initial point.
     */
    private double calculateMaxDeviation() {
        double maxDeviation = 0;
        for (int i = 0; i < mLatitudes.size(); i++) {
            double deviationX = Math.abs(mLongitudes.get(i) - mLongitudes.get(0));
            double deviationY = Math.abs(mLatitudes.get(i) - mLatitudes.get(0));
            double deviation = Math.sqrt(deviationX * deviationX + deviationY * deviationY);
            if (deviation > maxDeviation) {
                maxDeviation = deviation;
            }
        }
        return maxDeviation;
    }
}
