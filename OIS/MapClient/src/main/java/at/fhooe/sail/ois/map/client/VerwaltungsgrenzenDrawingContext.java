package at.fhooe.sail.ois.map.client;

import javafx.scene.paint.Color;

/**
 * A concrete implementation of the abstract class ADrawingContext for administrative boundaries (Verwaltungsgrenzen).
 * This class initializes a set of predefined presentation schemas for various administrative boundary types.
 */
public class VerwaltungsgrenzenDrawingContext extends ADrawingContext {

    /**
     * Initializes the presentation schemas for administrative boundaries data.
     * This method populates the mContext hashtable with predefined presentation schemas for various administrative boundary types.
     */
    @Override
    protected void initSchemata() {
        mContext.put(0, new PresentationSchema(Color.BLACK, null, 1.0f));
    }
}
