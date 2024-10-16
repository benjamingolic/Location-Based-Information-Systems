package at.fhooe.sail.ois.map.client;

import javafx.scene.paint.Color;
import java.util.Hashtable;

/**
 * Abstract base class for defining drawing contexts with presentation schemas.
 * This class provides a mechanism to manage presentation schemas for different types
 * of objects. It includes methods to retrieve specific or default schemas and an
 * abstract method to initialize the schemas.
 */
public abstract class ADrawingContext {

    /**
     * Hashtable to store presentation schemas mapped by integer type identifiers.
     */
    protected Hashtable<Integer, PresentationSchema> mContext;

    /**
     * Constructor for the ADrawingContext class.
     * Initializes the hashtable and calls the abstract method to initialize the schemas.
     */
    public ADrawingContext() {
        mContext = new Hashtable<>();
        initSchemata();
    }

    /**
     * Retrieves the presentation schema for a given type.
     *
     * @param _type the type identifier for which to retrieve the schema.
     * @return the presentation schema associated with the given type, or the default schema if not found.
     */
    public PresentationSchema getSchema(int _type) {
        return mContext.getOrDefault(_type, getDefaultSchema());
    }

    /**
     * Retrieves the default presentation schema.
     *
     * @return the default presentation schema.
     */
    public PresentationSchema getDefaultSchema() {
        return new PresentationSchema(Color.BLACK, Color.WHITE, 1.0f);
    }

    /**
     * Abstract method to initialize the presentation schemas.
     * This method should be implemented by subclasses to define the specific schemas
     * for different types.
     */
    protected abstract void initSchemata();
}
