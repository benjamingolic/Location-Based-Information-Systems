package at.fhooe.sail.ois.map.client;

import javafx.scene.paint.Color;

/**
 * Concrete implementation of ADrawingContext for dummy data.
 * This class initializes a set of predefined presentation schemas for different
 * type identifiers. Each schema defines the fill color, outline color, and stroke width
 * for rendering the objects of the corresponding type.
 */
public class DummyDrawingContext extends ADrawingContext {

    /**
     * Initializes the presentation schemas for dummy data.
     * This method populates the mContext hashtable with predefined presentation schemas
     * for specific type identifiers.
     */
    @Override
    protected void initSchemata() {
        mContext.put(233, new PresentationSchema(Color.BLACK, Color.WHITE, 1.0f));
        mContext.put(931, new PresentationSchema(Color.BLACK, Color.RED, 1.0f));
        mContext.put(932, new PresentationSchema(Color.RED, Color.ORANGE, 1.0f));
        mContext.put(1101, new PresentationSchema(Color.GREEN, Color.MAGENTA, 1.0f));
    }
}