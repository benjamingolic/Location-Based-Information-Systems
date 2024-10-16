package at.fhooe.sail.ois.gnss;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * TimeView is a custom Pane that implements the IPositionUpdateListener interface to display the current GNSS time.
 * It uses a Label to show the formatted time in HH:MM:SS format.
 */
public class TimeView extends Pane implements IPositionUpdateListener {

    private Label mTimeLabel;
    private NMEAInfo mInfo;

    /**
     * Constructs a TimeView and initializes the Label for displaying time.
     */
    public TimeView() {
        initializeTimeLabel();
        StackPane stack = createStackPane();
        getChildren().add(stack);
    }

    /**
     * Initializes the Label used for displaying the time.
     */
    private void initializeTimeLabel() {
        mTimeLabel = new Label();
        mTimeLabel.setFont(new Font("Arial", 24));
        mTimeLabel.setTextFill(Color.BLACK);
    }

    /**
     * Creates a StackPane to contain the time Label and sets its alignment and size properties.
     *
     * @return The configured StackPane.
     */
    private StackPane createStackPane() {
        StackPane stack = new StackPane();
        stack.prefWidthProperty().bind(widthProperty());
        stack.prefHeight(150);
        StackPane.setAlignment(mTimeLabel, Pos.CENTER);
        stack.getChildren().add(mTimeLabel);
        return stack;
    }

    /**
     * Called when the position is updated. This method updates the internal NMEAInfo object
     * and triggers an update of the time Label on the JavaFX application thread.
     *
     * @param info The new NMEAInfo object containing the updated GNSS data.
     */
    @Override
    public void update(NMEAInfo info) {
        this.mInfo = info;
        Platform.runLater(this::updateTimeLabel);
    }


    /**
     * Updates the time Label with the formatted time from the NMEAInfo object.
     */
    private void updateTimeLabel() {
        if (mInfo != null && mInfo.getMTime() != null) {
            String time = mInfo.getMTime();
            if (time.length() >= 6) {
                String formattedTime = String.format("%s:%s:%s", time.substring(0, 2), time.substring(2, 4), time.substring(4, 6));
                mTimeLabel.setText(formattedTime);
            }
        }
    }
}
