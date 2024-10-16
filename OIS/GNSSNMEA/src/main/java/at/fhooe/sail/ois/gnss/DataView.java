package at.fhooe.sail.ois.gnss;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.stream.Collectors;

/**
 * DataView is a custom Pane that implements the IPositionUpdateListener interface to display GNSS data.
 * It uses a GridPane to organize and display the various parameters such as Latitude, Longitude, Altitude,
 * PDOP, HDOP, VDOP, Fix Type, Visible Satellites, and Used Satellites.
 */
public class DataView extends Pane implements IPositionUpdateListener {
    private GridPane mGridPane;
    private NMEAInfo mInfo;

    /**
     * Called when the position is updated. This method updates the internal NMEAInfo object and triggers
     * the redraw of the GridPane on the JavaFX application thread.
     *
     * @param info The new NMEAInfo object containing the updated GNSS data.
     */
    @Override
    public void update(NMEAInfo info) {
        this.mInfo = info;
        Platform.runLater(this::draw);
    }

    /**
     * Clears the current children of the Pane, initializes the GridPane, populates it with labels and values,
     * and adds the GridPane to the Pane.
     */
    private void draw() {
        getChildren().clear();
        initializeGridPane();
        populateGridPane();
        getChildren().add(mGridPane);
    }

    /**
     * Initializes the GridPane with padding, horizontal and vertical gaps, and binds its width to the Pane's width.
     */
    private void initializeGridPane() {
        mGridPane = new GridPane();
        mGridPane.setPadding(new Insets(15));
        mGridPane.setHgap(15);
        mGridPane.setVgap(10);
        mGridPane.prefWidthProperty().bind(widthProperty());
    }

    /**
     * Populates the GridPane by adding labels and corresponding values for the GNSS data.
     */
    private void populateGridPane() {
        addLabelsToGridPane();
        addValuesToGridPane();
    }

    /**
     * Adds labels for the GNSS data parameters to the GridPane.
     */
    private void addLabelsToGridPane() {
        Label[] labels = {
                new Label("Latitude: "), new Label("Longitude: "), new Label("Altitude: "),
                new Label("PDOP: "), new Label("HDOP: "), new Label("VDOP: "),
                new Label("Fix Type: "), new Label("Visible Satellites: "), new Label("Used Satellites: ")
        };

        for (int i = 0; i < 3; i++) {
            mGridPane.add(labels[i], 0, i);
            mGridPane.add(labels[i + 3], 2, i);
            mGridPane.add(labels[i + 6], 4, i);
        }
    }

    /**
     * Adds values for the GNSS data parameters to the GridPane.
     */
    private void addValuesToGridPane() {
        Label latValTxt = new Label(String.format("%.6f", mInfo.getMLat()));
        Label lonValTxt = new Label(String.format("%.6f", mInfo.getMLon()));
        Label altValTxt = new Label(mInfo.getMHeight() + "m");
        Label pdopValTxt = new Label(String.valueOf(mInfo.getMPDOP()));
        Label hdopValTxt = new Label(String.valueOf(mInfo.getMHDOP()));
        Label vdopValTxt = new Label(String.valueOf(mInfo.getMVDOP()));
        Label fixTypeValTxt = new Label(mInfo.getMFixQuality());
        Label visSatValTxt = new Label(String.valueOf(mInfo.getMSatelliteCount()));
        String usedSatIds = mInfo.getMSatellites().stream()
                .filter(SatelliteInfo::isMIsUsed)
                .map(sat -> String.valueOf(sat.getMId()))
                .collect(Collectors.joining(", "));
        Label usedSatValTxt = new Label(usedSatIds);

        Label[] values = {
                latValTxt, lonValTxt, altValTxt,
                pdopValTxt, hdopValTxt, vdopValTxt,
                fixTypeValTxt, visSatValTxt, usedSatValTxt
        };

        for (int i = 0; i < 3; i++) {
            mGridPane.add(values[i], 1, i);
            mGridPane.add(values[i + 3], 3, i);
            mGridPane.add(values[i + 6], 5, i);
        }
    }
}
