package at.fhooe.sail.ois.map.server.feature;

import at.fhooe.sail.ois.map.client.Matrix;
import at.fhooe.sail.ois.map.client.PresentationSchema;

import java.awt.*;

/**
 * Represents an area as a part of a geographic object.
 * This class holds the geometry of the area and provides methods to access its bounds and draw it onto a graphics context.
 */
public class Area extends GeoObjectPart{
    private Polygon mGeometry;

    /**
     * Constructs an Area object with the specified geometry.
     *
     * @param _geometry the polygon representing the geometry of the area.
     */
    public Area(Polygon _geometry){
        this.mGeometry = _geometry;
    }

    /**
     * Gets the geometry of the area.
     *
     * @return the polygon representing the geometry of the area.
     */
    public Polygon getGeometry(){
        return mGeometry;
    }

    /**
     * Gets the bounding rectangle of the area's geometry.
     *
     * @return the bounding rectangle of the geometry.
     */
    public Rectangle getBounds(){
        return mGeometry.getBounds();
    }

    /**
     * Draws the area onto the provided Graphics2D context using the specified transformation matrix and presentation schema.
     *
     * @param _g the Graphics2D context to draw on.
     * @param _m the transformation matrix to apply to the geometry.
     * @param _schema the presentation schema defining the colors and line width.
     */
    @Override
    public void draw(Graphics2D _g, Matrix _m, PresentationSchema _schema){
        Polygon transformed = _m.multiply(mGeometry);
        java.awt.geom.Area area = new java.awt.geom.Area(transformed);
        for(GeoObjectPart hole : mHoles){
            if(hole instanceof Area){
                Polygon poly = _m.multiply(((Area) hole).getGeometry());
                area.subtract(new java.awt.geom.Area(poly));
            }
        }

        _g.setColor(_schema.getFillColor());
        _g.fill(area);
        _g.setColor(_schema.getLineColor());
        _g.setStroke(new BasicStroke(_schema.getLineWidth()));
        _g.draw(area);

    }
}
