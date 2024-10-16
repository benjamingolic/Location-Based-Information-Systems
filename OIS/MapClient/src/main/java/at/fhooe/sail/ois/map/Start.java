package at.fhooe.sail.ois.map;

import at.fhooe.sail.ois.map.client.GISView;

/**
 * The entry point for launching the GISView JavaFX application.
 * This class contains the main method which initiates the JavaFX application lifecycle.
 */
public class Start {

    /**
     * The main method that serves as the entry point of the Java application.
     * It launches the GISView JavaFX application.
     *
     * @param _argv Command-line arguments passed to the application.
     */
    public static void main(String[] _argv) {
        javafx.application.Application.launch(GISView.class, _argv);
    }
}