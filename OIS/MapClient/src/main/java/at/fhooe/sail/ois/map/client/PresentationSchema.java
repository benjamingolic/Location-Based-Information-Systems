package at.fhooe.sail.ois.map.client;

import at.fhooe.sail.ois.map.server.feature.GeoObject;

import java.awt.*;
import javafx.scene.paint.Color;

/**
 * Represents the presentation schema for drawing geometric objects.
 * This class holds the color and line width information for drawing geometric objects
 * and provides a method to paint these objects onto a graphics context using a transformation matrix.
 */
public class PresentationSchema {
    private Color mLineColor = null;
    private Color mFillColor = null;
    private float mLineWidth = -1.0f;

    /**
     * Constructs a PresentationSchema with the specified line color, fill color, and line width.
     *
     * @param _lineColor the color used for the lines of the shape.
     * @param _fillColor the color used to fill the shape.
     * @param _lineWidth the width of the lines.
     */
    public PresentationSchema(Color _lineColor, Color _fillColor, float _lineWidth) {
        mLineColor = _lineColor;
        mFillColor = _fillColor;
        mLineWidth = _lineWidth;
    }

    /**
     * Paints the given GeoObject onto the provided Graphics2D context using the specified transformation matrix.
     *
     * @param _g the Graphics2D context to draw on.
     * @param _obj the GeoObject to be painted.
     * @param _m the transformation matrix to apply to the GeoObject's polygon.
     */
    public void paint(Graphics2D _g, GeoObject _obj, Matrix _m) {

        _obj.getGeoObjectParts().forEach(geoObjectPart -> geoObjectPart.draw(_g, _m, this));
    }

    /**
     * Gets the line color as a java.awt.Color object.
     *
     * @return the line color, or null if no line color is set.
     */
    public java.awt.Color getLineColor() {
        if(mLineColor == null) return null;
        return new java.awt.Color((float) mLineColor.getRed(), (float) mLineColor.getGreen(), (float) mLineColor.getBlue(), (float) mLineColor.getOpacity());
    }

    /**
     * Gets the fill color as a java.awt.Color object.
     *
     * @return the fill color, or null if no fill color is set.
     */
    public java.awt.Color getFillColor() {
        if(mFillColor == null) return null;
        return new java.awt.Color((float) mFillColor.getRed(), (float) mFillColor.getGreen(), (float) mFillColor.getBlue(), (float) mFillColor.getOpacity());
    }

    /**
     * Gets the line width.
     *
     * @return the line width.
     */
    public float getLineWidth() {
        return mLineWidth;
    }
}
