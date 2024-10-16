package at.fhooe.sail.ois.gnss;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

/**
 * GNSSView is a JavaFX application that visualizes GNSS data from a simulator.
 * It sets up and displays various views such as TimeView, SatView, DeviationView, and DataView.
 */
public class GNSSView extends Application {

    private static final String mTitle = "Golić - GNSS";
    private static final String mSimulatorFile = "NMEA-data-1--Hgb-Statisch-nur-GPS.nmea";
    //private static final String mSimulatorFile = "NMEA-data-2--Engerwitzdorf-PlusCity.nmea";
    //private static final String mSimulatorFile = "NMEA-data-3--Materl-Position-Statisch.nmea";
    private static final int mSimulatorInterval = 1000;
    private static final String mFilter = "$GPGGA";
    private static final int mSceneWidth = 800;
    private static final int mSceneHeight = 550;

    private Scene mScene;
    private NMEAParser mParser;
    private GNSSSimulator mSimulator;

    /**
     * The main entry point for all JavaFX applications.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception If something goes wrong during initialization.
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: lightgray;");

        TimeView timeView = createTimeView(root);
        root.setTop(timeView);

        HBox centerPane = new HBox();
        centerPane.setStyle("-fx-background-color: lightgray;");
        SatView satView = createSatView(centerPane);
        DeviationView deviationView = createDeviationView(centerPane);

        centerPane.getChildren().addAll(satView, deviationView);
        root.setCenter(centerPane);

        DataView dataView = createDataView(root);
        root.setBottom(dataView);

        mScene = new Scene(root, mSceneWidth, mSceneHeight);

        configureStage(stage);
        stage.show();
        satView.init();
    }

    /**
     * Creates and configures the TimeView.
     *
     * @param root The root layout to which the TimeView will be added.
     * @return The configured TimeView.
     */
    private TimeView createTimeView(BorderPane root) {
        TimeView timeView = new TimeView();
        timeView.setStyle("-fx-background-color: yellow;");
        timeView.prefWidthProperty().bind(root.widthProperty());
        mParser.addPositionUpdateListener(timeView);
        return timeView;
    }

    /**
     * Creates and configures the SatView.
     *
     * @param root The parent layout to which the SatView will be added.
     * @return The configured SatView.
     */
    private SatView createSatView(HBox root) {
        SatView satView = new SatView();
        satView.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
        satView.prefWidthProperty().bind(root.widthProperty().divide(2));
        satView.prefHeightProperty().bind(root.heightProperty());
        mParser.addPositionUpdateListener(satView);
        return satView;
    }

    /**
     * Creates and configures the DeviationView.
     *
     * @param root The parent layout to which the DeviationView will be added.
     * @return The configured DeviationView.
     */
    private DeviationView createDeviationView(HBox root) {
        DeviationView deviationView = new DeviationView();
        deviationView.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
        deviationView.prefWidthProperty().bind(root.widthProperty().divide(2));
        deviationView.prefHeightProperty().bind(root.heightProperty());
        mParser.addPositionUpdateListener(deviationView);
        return deviationView;
    }

    /**
     * Creates and configures the DataView.
     *
     * @param root The root layout to which the DataView will be added.
     * @return The configured DataView.
     */
    private DataView createDataView(BorderPane root) {
        DataView dataView = new DataView();
        dataView.setStyle("-fx-background-color: cyan;");
        dataView.prefWidthProperty().bind(root.widthProperty());
        mParser.addPositionUpdateListener(dataView);
        return dataView;
    }

    /**
     * Configures the stage with the specified scene and title.
     *
     * @param stage The stage to configure.
     */
    private void configureStage(Stage stage) {
        stage.setTitle(mTitle);
        stage.setScene(mScene);
    }

    /**
     * Initializes the simulator and parser before the application starts.
     */
    @Override
    public void init() {
        try {
            initializeSimulatorAndParser();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Simulator file not found: " + mSimulatorFile, e);
        }
    }

    /**
     * Initializes the GNSS simulator and parser.
     *
     * @throws FileNotFoundException If the simulator file is not found.
     */
    private void initializeSimulatorAndParser() throws FileNotFoundException {
        mSimulator = new GNSSSimulator(mSimulatorFile, mSimulatorInterval, mFilter);
        mParser = new NMEAParser(mSimulator);
        mParser.start();
    }

    /**
     * The main method, which is the entry point for the Java application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
