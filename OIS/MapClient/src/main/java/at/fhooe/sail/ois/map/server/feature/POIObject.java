package at.fhooe.sail.ois.map.server.feature;

import at.fhooe.sail.ois.map.client.Matrix;
import at.fhooe.sail.ois.map.client.PresentationSchema;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents a point of interest (POI) object as a part of a geographic object.
 * This class holds the geometry and filename of the POI image and provides methods to draw it onto a graphics context.
 */
public class POIObject extends GeoObjectPart {

    private java.awt.Point mGeometry;
    private String mFilename;

    /**
     * Constructs a POIObject with the specified filename and geometry.
     *
     * @param _filename the filename of the POI image.
     * @param _geometry the point representing the geometry of the POI.
     */
    public POIObject(String _filename, java.awt.Point _geometry) {
        this.mGeometry = _geometry;
        this.mFilename = _filename;
    }


    /**
     * Draws the POI onto the provided Graphics2D context using the specified transformation matrix and presentation schema.
     *
     * @param _g the Graphics2D context to draw on.
     * @param _m the transformation matrix to apply to the geometry.
     * @param _schema the presentation schema defining the colors and line width.
     */
    public void draw(Graphics2D _g, Matrix _m, PresentationSchema _schema) {
        java.awt.Point p = _m.multiply(mGeometry);
        try {
            BufferedImage img = ImageIO.read(new File("/Users/benjamingolic/Documents/FH Hagenberg/2. & 4. Semester/OIS/OIS/MapClient/src/main/resources/" + mFilename));
            _g.drawImage(img, p.x, p.y, 35, 35 * img.getHeight(null) / img.getWidth(null), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the bounding box of the POI's geometry.
     *
     * @return the bounding box of the geometry as a Rectangle object.
     */
    public Rectangle getBounds() {
        return new Rectangle(mGeometry.x, mGeometry.y, 1, 1);
    }
}
