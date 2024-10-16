package at.fhooe.sail.ois.map.server.feature;

import at.fhooe.sail.ois.map.client.Matrix;
import at.fhooe.sail.ois.map.client.PresentationSchema;

import java.awt.*;
import java.util.Vector;

/**
 * Abstract class representing a part of a geographic object.
 * This class defines common behavior for all geometric object parts, including methods for drawing,
 * retrieving bounds, and managing holes within the geometric object.
 */
public abstract class GeoObjectPart {
    public Vector<GeoObjectPart> mHoles = new Vector<>();

    /**
     * Abstract method to draw the geometric object part onto a Graphics2D context using a transformation matrix and presentation schema.
     *
     * @param _g the Graphics2D context to draw on.
     * @param _m the transformation matrix to apply to the geometry.
     * @param _schema the presentation schema defining the colors and line width.
     */
    public abstract void draw(Graphics2D _g, Matrix _m, PresentationSchema _schema);

    /**
     * Abstract method to get the bounding rectangle of the geometric object part.
     *
     * @return the bounding rectangle of the geometry.
     */
    public abstract Rectangle getBounds();

    /**
     * Adds a hole to the geometric object part.
     * The hole is represented as a polygon and will be subtracted from the main geometry when drawn.
     *
     * @param _poly the polygon representing the hole.
     */
    public void addHole(Polygon _poly){
        Area a = new Area(_poly);
        mHoles.add(a);
    }
}
