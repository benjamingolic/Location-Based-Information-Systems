package at.fhooe.sail.ois.map.server.feature;

/**
 * Represents a point as a part of a geographic object.
 * This class holds the geometry of the point and provides methods to access its bounds and draw it onto a graphics context.
 */
public class Point extends GeoObjectPart{
    private java.awt.Point mGeometry;

    /**
     * Constructs a Point object with the specified geometry.
     *
     * @param _geometry the Point representing the geometry of the point.
     */
    public Point(java.awt.Point _geometry){
        this.mGeometry = _geometry;
    }

    /**
     * Gets the bounding box of the point's geometry.
     *
     * @return the bounding box of the geometry as a Rectangle object.
     */
    @Override
    public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle(mGeometry);
    }

    /**
     * Draws the point onto the provided Graphics2D context using the specified transformation matrix and presentation schema.
     *
     * @param _g the Graphics2D context to draw on.
     * @param _m the transformation matrix to apply to the geometry.
     * @param _schema the presentation schema defining the colors and line width.
     */
    @Override
    public void draw(java.awt.Graphics2D _g, at.fhooe.sail.ois.map.client.Matrix _m, at.fhooe.sail.ois.map.client.PresentationSchema _schema){
        java.awt.Point transformed = _m.multiply(mGeometry);
        _g.setColor(_schema.getLineColor());
        _g.fillOval(transformed.x, transformed.y, 5, 5);
    }
}
