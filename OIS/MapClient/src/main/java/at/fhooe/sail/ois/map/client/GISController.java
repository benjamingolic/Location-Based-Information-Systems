package at.fhooe.sail.ois.map.client;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls interactions for a geographic information system (GIS) model.
 * This controller handles action events, changes in properties, and mouse interactions.
 */
public class GISController {
    private final GISModel mModel;
    private final GISView mView;
    private ActionHandler mActionHandler;
    private ChangeHandler mChangeHandler;
    private MouseHandler mMouseHandler;
    private ScrollHandler mScrollHandler;
    private double mStartX, mStartY;
    private double mDeltaX, mDeltaY;
    private Rectangle mZoomRect = null;
    private List<Point> mSelectedPoints = new ArrayList<>();
    private KeyEventHandler mKeyEventHandler;

    /**
     * Constructs a GISController with a specified GISModel and GISView.
     * @param _model The GISModel to be controlled.
     * @param _view The GISView to be controlled.
     */
    public GISController(GISModel _model, GISView _view) {
        mModel = _model;
        mView = _view;
    }

    /**
     * Lazily initializes and returns the action handler for the controller.
     * @return An instance of the ActionHandler class.
     */
    public ActionHandler getActionHandler() {
        if (mActionHandler == null) {
            mActionHandler = new ActionHandler();
        }
        return mActionHandler;
    }

    /**
     * Lazily initializes and returns the change handler for the controller.
     * @return An instance of the ChangeHandler class.
     */
    public ChangeHandler getChangeHandler() {
        if (mChangeHandler == null) {
            mChangeHandler = new ChangeHandler();
        }
        return mChangeHandler;
    }

    /**
     * Lazily initializes and returns the mouse handler for the controller.
     * @return An instance of the MouseHandler class.
     */
    public MouseHandler getMouseHandler() {
        if (mMouseHandler == null) {
            mMouseHandler = new MouseHandler();
        }
        return mMouseHandler;
    }

    /**
     * Lazily initializes and returns the scroll handler for the controller.
     * @return An instance of the ScrollHandler class.
     */
    public ScrollHandler getScrollHandler() {
        if (mScrollHandler == null) {
            mScrollHandler = new ScrollHandler();
        }
        return mScrollHandler;
    }

    /**
     * Lazily initializes and returns the key event handler for the controller.
     * @return An instance of the KeyEventHandler class.
     */
    public KeyEventHandler getKeyEventHandler() {
        if (mKeyEventHandler == null) {
            mKeyEventHandler = new KeyEventHandler();
        }
        return mKeyEventHandler;
    }

    /**
     * Handles action events for various buttons.
     * This class implements the EventHandler interface for handling ActionEvents
     * triggered by button clicks. It performs different actions based on the button's ID.
     */
    public class ActionHandler implements EventHandler<ActionEvent> {
        /**
         * Handles the action event for buttons.
         * This method is called when a button is clicked. It performs different actions
         * based on the ID of the button, such as loading data, zooming, scrolling, and rotating.
         * After performing the action, it updates the scale field.
         *
         * @param _event the action event that triggered the button click.
         */
        @Override
        public void handle(ActionEvent _event) {
            String id = ((Button) _event.getSource()).getId();

            switch (id) {
                case "Load":
                    System.out.println("Load button clicked");
                    String selectedServer = mView.getSelectedServer();
                    System.out.println("Loading Data from: " + selectedServer);
                    mModel.loadData(selectedServer);
                    mModel.loadPOI();
                    mModel.calculateScale();
                    break;
                case "ZTF":
                    System.out.println("ZoomToFit button clicked");
                    mModel.zoomToFit();
                    mModel.calculateScale();
                    break;
                case "ZoomIn":
                    System.out.println("ZoomIn button clicked");
                    mModel.zoom(1.3);
                    mModel.calculateScale();
                    break;
                case "ZoomOut":
                    System.out.println("ZoomOut button clicked");
                    mModel.zoom(1 / 1.3);
                    mModel.calculateScale();
                    break;
                case "ScrollUp":
                    System.out.println("ScrollUp button clicked");
                    mModel.scrollVertical(20);
                    break;
                case "ScrollDown":
                    System.out.println("ScrollDown button clicked");
                    mModel.scrollVertical(-20);
                    break;
                case "ScrollLeft":
                    System.out.println("ScrollLeft button clicked");
                    mModel.scrollHorizontal(20);
                    break;
                case "ScrollRight":
                    System.out.println("ScrollRight button clicked");
                    mModel.scrollHorizontal(-20);
                    break;
                case "Rotate":
                    System.out.println("Rotate button clicked");
                    mModel.rotate(15);
                    break;
                case "POI":
                    System.out.println("POI button clicked");
                    mModel.togglePOI();
                    break;
                case "Store":
                    System.out.println("Store button clicked");
                    mModel.storeImg();
                    break;
                case "Sticky":
                    System.out.println("Sticky button clicked");
                    mModel.toggleStickyMode();
                    if (mModel.isStickyModeOn()) {
                        mModel.setStickyBBox(mView.getVisibleArea());
                    }
                    mModel.loadData(mView.getSelectedServer());
                    break;
                default:
                    System.out.println("Unknown action: " + id);
            }
        }
    }

    /**
     * Handles change events for properties of type Number.
     * This class implements the ChangeListener interface for handling changes in
     * properties of type Number. It updates the model's width or height based on
     * the property name and triggers an update of the scale field.
     */
    public class ChangeHandler implements ChangeListener<Number> {
        /**
         * Handles the change event for a Number property.
         * This method is called when the observed property is changed. It checks if the
         * observable property is a ReadOnlyDoubleProperty and updates the model's width
         * or height based on the property name. It also triggers an update of the scale field.
         *
         * @param _observable the observable value that has changed.
         * @param _oldValue the old value of the property.
         * @param _newValue the new value of the property.
         */
        @Override
        public void changed(javafx.beans.value.ObservableValue<? extends Number> _observable, Number _oldValue, Number _newValue) {
            if (_observable instanceof ReadOnlyDoubleProperty dProp) {
                double val = dProp.doubleValue();
                String name = dProp.getName();
                if (name.equalsIgnoreCase("width")) {
                    mModel.setWidth(val);
                } else if (name.equalsIgnoreCase("height")) {
                    mModel.setHeight(val);
                }
            }
        }
    }

    /**
     * Handles mouse events for zooming and panning functionality.
     * This class implements the EventHandler interface for MouseEvent to handle
     * mouse interactions for zooming and panning within a map view. It distinguishes
     * between primary (left) and secondary (right) mouse button actions.
     */
    public class MouseHandler implements EventHandler<MouseEvent> {
        /**
         * Handles the mouse event for zooming and panning.
         * This method processes different types of mouse events such as mouse pressed,
         * dragged, and released. It handles zooming when the primary mouse button is
         * used and panning when the secondary mouse button is used.
         *
         * @param _e the mouse event that triggered the action.
         */
        @Override
        public void handle(MouseEvent _e) {
            EventType<MouseEvent> type = (EventType<MouseEvent>) _e.getEventType();

            if (_e.getButton() == MouseButton.PRIMARY) {
                switch (type.toString()) {
                    case "MOUSE_PRESSED":
                        if (mZoomRect == null) {
                            mZoomRect = new Rectangle();
                        }

                        mView.mScene.setCursor(Cursor.CROSSHAIR);
                        mStartX = _e.getX();
                        mStartY = _e.getY();

                        clipboardHandler(_e);

                        break;
                    case "MOUSE_DRAGGED":
                        int currentX = (int) _e.getX();
                        int currentY = (int) _e.getY();
                        mZoomRect = new Rectangle(new Point((int) mStartX, (int) mStartY));
                        mZoomRect.add(new Point(currentX, currentY));
                        mView.drawXOR(mZoomRect);
                        break;
                    case "MOUSE_RELEASED":
                        mView.mScene.setCursor(Cursor.DEFAULT);
                        mView.clearXOR();
                        if (mZoomRect.getWidth() > 10 && mZoomRect.getHeight() > 10) { // Prevent too small rectangles
                            mModel.zoomRect(mZoomRect.getBounds());
                            mModel.calculateScale();
                        }
                        mZoomRect = null;
                        break;
                }
            } else if (_e.getButton() == MouseButton.SECONDARY) {
                switch (type.toString()) {
                    case "MOUSE_PRESSED":
                        mView.mScene.setCursor(Cursor.CLOSED_HAND);
                        mStartX = _e.getX();
                        mStartY = _e.getY();
                        mDeltaX = 0;
                        mDeltaY = 0;
                        break;
                    case "MOUSE_DRAGGED":
                        int x = (int) _e.getX();
                        int y = (int) _e.getY();
                        mDeltaX = x - mStartX;
                        mDeltaY = y - mStartY;
                        mView.translate(mDeltaX, mDeltaY);
                        break;
                    case "MOUSE_RELEASED":
                        mView.mScene.setCursor(Cursor.DEFAULT);
                        mModel.scrollHorizontal((int) mDeltaX);
                        mModel.scrollVertical((int) mDeltaY);
                        break;
                }
            }
        }
    }

    /**
     * Handles scroll events for zooming functionality.
     * This class implements the EventHandler interface for ScrollEvent to handle
     * zooming in and out based on the scroll direction. The zoom point is determined
     * by the cursor position at the time of the scroll event.
     */
    public class ScrollHandler implements EventHandler<ScrollEvent> {
        /**
         * Handles the scroll event to perform zooming.
         * This method calculates the zoom factor based on the scroll direction and
         * applies the zoom to the model at the location of the cursor. It also updates
         * the scale field in the view to reflect the new zoom level.
         *
         * @param _e the scroll event that triggered the zoom action.
         */
        @Override
        public void handle(ScrollEvent _e) {
            double deltaY = _e.getDeltaY();
            Point zoomPoint = new Point((int) _e.getX(), (int) _e.getY());
            double zoomFactor;
            if (deltaY > 0) {
                zoomFactor = 1.1;
            } else {
                zoomFactor = 1 / 1.1;
            }
            mModel.zoom(zoomPoint, zoomFactor);
            mModel.calculateScale();
        }
    }

    /**
     * Handles key events for text fields.
     * This class implements the EventHandler interface for KeyEvent to handle key
     * interactions, specifically to capture Enter key presses in text fields and
     * perform actions based on the entered text.
     */
    public class KeyEventHandler implements EventHandler<KeyEvent> {
        /**
         * Handles the key event for text fields.
         * This method is called when a key is pressed in a text field. It checks if the
         * Enter key is pressed and processes the text to update the model's scale accordingly.
         *
         * @param _event the key event that triggered the action.
         */
        @Override
        public void handle(KeyEvent _event) {
            if (_event.getCode() == KeyCode.ENTER && _event.getSource() instanceof TextField) {
                TextField txtField = (TextField) _event.getSource();
                String text = txtField.getText();
                System.out.println("Textfield containment: " + text);
                try {
                    int scale = Integer.parseInt(text.replace("1:", "").replace("\n", ""));
                    System.out.println("Scale entered: " + scale);

                    zoomToScale(scale);
                    mModel.calculateScale();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid scale: " + text);
                }
            }
        }
    }

    /**
     * Handles the clipboard operations when a mouse event occurs.
     * This method retrieves the map point corresponding to the mouse click location,
     * manages the selection of points based on whether the Control key is pressed,
     * constructs a string representation of the selected points, and copies this
     * representation to the system clipboard.
     *
     * @param _e the mouse event that triggered the clipboard operation.
     */
    private void clipboardHandler(MouseEvent _e) {
        Point worldPoint = mModel.getMapPoint(new Point((int) mStartX, (int) mStartY));
        if (_e.isControlDown()) {
            mSelectedPoints.add(worldPoint);
        } else {
            mSelectedPoints.clear();
            mSelectedPoints.add(worldPoint);
        }
        StringBuilder coordinates = new StringBuilder();
        for (Point pt : mSelectedPoints) {
            coordinates.append("(").append(pt.x).append(",").append(pt.y).append(")\n");
        }
        StringSelection stringSelection = new StringSelection(coordinates.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    /**
     * Zooms the view to a specified _scale.
     * This method sets the zoom level in the model to the specified _scale and then
     * updates the _scale field in the view to reflect the new zoom level.
     *
     * @param _scale the new _scale to zoom to.
     */
    public void zoomToScale(int _scale) {
        mModel.zoomToScale(_scale);
    }
}
