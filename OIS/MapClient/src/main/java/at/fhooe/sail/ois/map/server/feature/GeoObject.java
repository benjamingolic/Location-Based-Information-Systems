package at.fhooe.sail.ois.map.server.feature;

import java.awt.*;
import java.util.Vector;

/**
 * Represents a geographic object consisting of multiple geometric parts.
 * This class holds the identifier, type, and parts of the geographic object and provides methods to access its properties and bounds.
 */
public class GeoObject {
    private String mId;
    private int mType;
    private Vector<GeoObjectPart> mGeoObjectParts;

    /**
     * Constructs a GeoObject with the specified id, type, and parts.
     *
     * @param _id The id of the object.
     * @param _type The type of the object.
     * @param _geoObjectParts Parts of the GeoObject.
     */
    public GeoObject(String _id, int _type, Vector<GeoObjectPart> _geoObjectParts) {
        this.mId = _id;
        this.mType = _type;
        //this.mPoly = _poly;
        this.mGeoObjectParts = _geoObjectParts;
    }

    /**
     * Gets the type of the GeoObject.
     *
     * @return The type of the object.
     */
    public int getmType() {
        return mType;
    }

    /**
     * Gets the bounding box of the geometry.
     *
     * @return The bounding box of the geometry as a Rectangle object.
     * @see java.awt.Rectangle
     */
    public Rectangle getBounds() {
        //return mPoly.getBounds();
        Rectangle bounds = mGeoObjectParts.get(0).getBounds();
        for (int i = 1; i < mGeoObjectParts.size(); i++) {
            bounds = bounds.union(mGeoObjectParts.get(i).getBounds());
        }
        return bounds;
    }

    /**
     * Returns the internal information of the GeoObject as a string.
     *
     * @return The content of the object in the form of a string.
     */
    public String toString() {
        return "GeoObject{id='" + mId + "', type=" + mType + ", bounds=" + getBounds() + "}";
    }

    /**
     * Gets the parts of the GeoObject.
     *
     * @return The parts of the GeoObject.
     */
    public Vector<GeoObjectPart> getGeoObjectParts() {
        return mGeoObjectParts;
    }
}
