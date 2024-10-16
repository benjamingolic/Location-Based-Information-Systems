package at.fhooe.sail.ois.map.server.feature;

import at.fhooe.sail.ois.map.client.Matrix;
import at.fhooe.sail.ois.map.client.PresentationSchema;

import java.awt.*;
import java.util.Vector;
import java.awt.Point;

/**
 * Represents a line as a part of a geographic object.
 * This class holds the geometry of the line and provides methods to access its bounds and draw it onto a graphics context.
 */
public class Line extends GeoObjectPart{
    private Vector<Point> mGeometry;

    /**
     * Constructs a Line object with the specified geometry.
     *
     * @param _geometry the vector of points representing the geometry of the line.
     */
    public Line(Vector<Point> _geometry){
        this.mGeometry = _geometry;
    }

    /**
     * Gets the bounding box of the line's geometry.
     *
     * @return the bounding box of the geometry as a Rectangle object.
     */
    @Override
    public Rectangle getBounds(){
        Rectangle fullBounds = new Rectangle(mGeometry.get(0));
        for (int i = 1; i < mGeometry.size(); i++) {
            fullBounds = fullBounds.union(new Rectangle(mGeometry.get(i)));
        }
        return fullBounds;
    }

    /**
     * Draws the line onto the provided Graphics2D context using the specified transformation matrix and presentation schema.
     *
     * @param _g the Graphics2D context to draw on.
     * @param _m the transformation matrix to apply to the geometry.
     * @param _schema the presentation schema defining the colors and line width.
     */
    @Override
    public void draw(Graphics2D _g, Matrix _m, PresentationSchema _schema){
        int numPoints = mGeometry.size();
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];

        for (int i = 0; i < numPoints; i++) {
            Point transformed = _m.multiply(mGeometry.get(i));
            xPoints[i] = transformed.x;
            yPoints[i] = transformed.y;
        }

        _g.setStroke(new BasicStroke(_schema.getLineWidth()));
        _g.setColor(_schema.getLineColor());
        _g.drawPolyline(xPoints, yPoints, numPoints);
    }
}
