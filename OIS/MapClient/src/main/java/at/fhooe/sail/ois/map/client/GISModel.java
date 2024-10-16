package at.fhooe.sail.ois.map.client;

import at.fhooe.sail.ois.map.server.DummyGIS;
import at.fhooe.sail.ois.map.server.OSMServer;
import at.fhooe.sail.ois.map.server.VerwaltungsgrenzenServer;
import at.fhooe.sail.ois.map.server.feature.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/**
 * This class represents a geographic information system (GIS) model
 * that manages and displays graphical elements like polygons on a map.
 */
public class GISModel {
    private IDataObserver mObserver;
    private BufferedImage mImage;
    private int mWidth = 640;
    private int mHeight = 410;
    private final ArrayList<GeoObject> mData = new ArrayList<>();
    private Matrix mTransformationMatrix = new Matrix();
    private ADrawingContext mDrawingContext;
    private int mScale = 0;
    private Vector<POIObject> mPois = new Vector<>();
    private boolean mShowPOIs = true;
    private int mStoredImagesCounter = 0;
    private boolean mIsStickyModeOn = false;
    private Rectangle mStickyBBox = null;

    /**
     * Constructor for the GISModel class.
     * Initializes the GISModel with default settings.
     */
    public GISModel() {}

    /**
     * Repaints the image buffer by drawing all polygons in the data list.
     * Initializes the canvas if not already initialized.
     */
    public void repaint() {
        if (mImage == null) {
            mImage = initCanvas();
        }

        Graphics2D g = (Graphics2D) mImage.getGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, mImage.getWidth(), mImage.getHeight());
        g.setColor(Color.BLACK);

        for (GeoObject obj : mData) {
            PresentationSchema schema = mDrawingContext.getSchema(obj.getmType());
            schema.paint(g, obj, mTransformationMatrix);
        }

        if (mShowPOIs) {
            for (POIObject poi : mPois) {
                poi.draw(g, mTransformationMatrix, null);
            }
        }

        update(mImage);
    }

    /**
     * Initializes the image canvas with specified width and height.
     * @return A new BufferedImage object.
     */
    public BufferedImage initCanvas() {
        return new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Sets up a transformation matrix to scale, translate, and mirror
     * polygons so that they fit within a defined rectangle (_winBounds).
     * @param _winBounds The display area in screen coordinates.
     */
    public void zoomRect(Rectangle _winBounds) {
        if (mData.isEmpty()) {
            return;
        }

        mTransformationMatrix = Matrix.translate(mWidth / 2.0, mHeight / 2.0)
                .multiply(Matrix.scale(Math.min((double) mWidth / _winBounds.width, (double) mHeight / _winBounds.height)))
                .multiply(Matrix.translate(-(_winBounds.getCenterX()), -(_winBounds.getCenterY())))
                .multiply(mTransformationMatrix);

        repaint();
    }

    /**
     * Converts a point from screen coordinates to map coordinates.
     * @param _pt The point in screen coordinates.
     * @return The same point in map coordinates.
     */
    public Point getMapPoint(Point _pt) {
        return mTransformationMatrix.invers().multiply(_pt);
    }

    /**
     * Generates a random positioned house-shaped polygon and adds it to the map.
     * @deprecated This method is deprecated and may be removed in future versions.
     */
    @Deprecated
    public void generateRndHouse() {
        int x = (int) (Math.random() * (mWidth - 30));
        int y = (int) (Math.random() * (mHeight - 40));
        Polygon house = makeCustomPolygon(x, y);
        Vector<GeoObjectPart> houseObjectVector = new Vector<>((Collection<? extends GeoObjectPart>) house);
        GeoObject houseObject = new GeoObject("RandomHouse", 0, houseObjectVector);
        mData.add(houseObject);
        repaint();
    }

    /**
     * Generates a house-shaped polygon at the specified coordinates and adds it to the map.
     * @param _x The x-coordinate for the new house.
     * @param _y The y-coordinate for the new house.
     * @deprecated This method is deprecated and may be removed in future versions.
     */
    @Deprecated
    public void generatePositionedHouse(int _x, int _y) {
        if (_x < 0 || _x > (mWidth - 30) || _y < 0 || _y > (mHeight - 40)) {
            return;
        }

        Polygon house = makeCustomPolygon(_x, _y);
        Vector<GeoObjectPart> houseObjectVector = new Vector<>((Collection<? extends GeoObjectPart>) house);
        GeoObject houseObject = new GeoObject("PositionedHouse", 0, houseObjectVector);
        mData.add(houseObject);
        repaint();
    }

    /**
     * Creates a custom polygon shaped like a house.
     * @param _x The x-coordinate of the bottom-left corner of the house.
     * @param _y The y-coordinate of the bottom-left corner of the house.
     * @return The created house-shaped polygon.
     */
    private Polygon makeCustomPolygon(int _x, int _y) {
        Polygon polygon = new Polygon();
        polygon.addPoint(_x, _y);
        polygon.addPoint(_x + 30, _y);
        polygon.addPoint(_x + 30, _y + 20);
        polygon.addPoint(_x + 15, _y + 40);
        polygon.addPoint(_x, _y + 20);
        polygon.addPoint(_x, _y);
        return polygon;
    }

    /**
     * Notifies the observer with the updated image.
     * @param _house The updated BufferedImage to be sent to the observer.
     */
    protected void update(BufferedImage _house) {
        mObserver.update(_house);
    }

    /**
     * Adds an observer to the model.
     * @param _observer The observer to add.
     */
    public void addMapObserver(IDataObserver _observer) {
        mObserver = _observer;
    }

    /**
     * Sets a new width for the image canvas, adjusting the width and reinitializing the canvas if needed.
     * @param _width The new width to set.
     */
    public void setWidth(double _width) {
        if (_width > 0 && _width != mWidth) {
            mWidth = (int) _width;
            mImage = null;
            repaint();
        }
    }

    /**
     * Sets a new height for the image canvas, adjusting the height and reinitializing the canvas if needed.
     * @param _height The new height to set.
     */
    public void setHeight(double _height) {
        if (_height > 0 && _height != mHeight) {
            mHeight = (int) _height;
            mImage = null;
            repaint();
        }
    }

    /**
     * Sets up a transformation matrix to scale, translate, and mirror
     * polygons so that they fit entirely within the display area.
     */
    public void zoomToFit() {
        if (mData.isEmpty()) {
            return;
        }
        Rectangle bounds = getMapBounds(new Vector<>(mData));
        mTransformationMatrix = Matrix.zoomToFit(bounds, new Rectangle(0, 0, mWidth, mHeight));
        repaint();
    }

    /**
     * Changes the internal transformation matrix to zoom in or out
     * to the center of the display area.
     *
     * @param _factor The factor to zoom in or out by.
     */
    public void zoom(double _factor) {
        Point center = new Point(mWidth / 2, mHeight / 2);
        zoom(center, _factor);
    }

    /**
     * Changes the internal transformation matrix to zoom in or out
     * at the specified point.
     *
     * @param _pt The point to zoom in or out at.
     * @param _factor The factor to zoom in or out by.
     */
    public void zoom(Point _pt, double _factor) {
        mTransformationMatrix = Matrix.zoomPoint(mTransformationMatrix, _pt, _factor);
        System.out.println("Transformation Matrix after zoom: " + mTransformationMatrix);
        repaint();
    }

    /**
     * Calculates the bounding box of the given polygons.
     *
     * @param _poly The polygons to calculate the bounding box for.
     * @return The bounding box.
     */
    public Rectangle getMapBounds(Vector<GeoObject> _poly) {
        Rectangle rect = _poly.firstElement().getBounds();
        for (GeoObject obj : _poly) {
            rect = rect.union(obj.getBounds());
        }
        return rect;
    }

    /**
     * Changes the internal transformation matrix to scroll horizontally.
     *
     * @param _delta The distance to scroll horizontally.
     */
    public void scrollHorizontal(int _delta) {
        mTransformationMatrix = Matrix.translate(_delta, 0).multiply(mTransformationMatrix);
        repaint();
    }

    /**
     * Changes the internal transformation matrix to scroll vertically.
     *
     * @param _delta The distance to scroll vertically.
     */
    public void scrollVertical(int _delta) {
        mTransformationMatrix = Matrix.translate(0, _delta).multiply(mTransformationMatrix);
        repaint();
    }

    /**
     * Rotates the view by the specified angle (in degrees).
     * @param angle The angle in degrees to rotate by.
     */
    public void rotate(double angle) {
        double radians = Math.toRadians(angle);

        Point center = new Point(mWidth / 2, mHeight / 2);
        Matrix translateToOrigin = Matrix.translate(-center.x, -center.y);
        Matrix rotationMatrix = Matrix.rotate(radians);
        Matrix translateBack = Matrix.translate(center.x, center.y);

        mTransformationMatrix = translateBack.multiply(rotationMatrix).multiply(translateToOrigin).multiply(mTransformationMatrix);

        repaint();
    }

    /**
     * Loads data from the specified server.
     * @param _server The server to load data from.
     */
    public void loadData(String _server) {
        switch (_server) {
            case "DummyGIS":
                loadGISData();
                break;
            case "Verwaltungsgrenzen-3857":
                loadGeoData();
                break;
            case "OSM-Server-HGB":
                loadOSMData();
                break;
            default:
                System.out.println("Unknown server type: " + _server);
        }
    }

    /**
     * Loads data from the DummyGIS server and adds it to the mData list.
     */
    public void loadGISData() {
        DummyGIS mDummyGIS = new DummyGIS();
        if (mDummyGIS.init()) {
            mDrawingContext = mDummyGIS.getDrawingContext();
            Vector<GeoObject> geoObjects = mDummyGIS.extractData("SELECT * FROM data WHERE type in (233, 931, 932, 933, 934, 1101)");
            if (geoObjects != null) {
                mData.clear();
                mData.addAll(geoObjects);
                repaint();
            }
        }
    }

    /**
     * Calculates the current scale of the map.
     *
     * @return The current map scale.
     */
    protected int calculateScale() {
        Point2D.Double vector = new Point2D.Double(0, 1.0); // 1 cm vector
        Point2D.Double vector_transformed = mTransformationMatrix.cleanTranslation().multiply(vector);
        int mDotPerInch = 72;
        double lengthA = mDotPerInch / 2.54;
        double lengthB = vector.distance(0, 0);
        double lengthC = vector_transformed.distance(0, 0);
        double scale = lengthA * lengthB / lengthC;

        mScale = (int) scale;
        updateScale(mScale);

        System.out.println("Scale: " + mScale);
        return mScale;
    }

    /**
     * Updates the observer with the new scale.
     * @param _scale The new scale to update.
     */
    protected void updateScale(int _scale) {
        mObserver.updateScale(_scale);
    }

    /**
     * Sets the zoom to a specific scale.
     * @param scale The scale to set.
     */
    public void zoomToScale(int scale) {
        zoom((mScale / (double) scale));
        repaint();
    }

    /**
     * Loads data from the Verwaltungsgrenzen server.
     */
    public void loadGeoData() {
        VerwaltungsgrenzenServer server = new VerwaltungsgrenzenServer();
        mDrawingContext = server.getVerwaltungsgrenzenDrawingContext();
        Vector<GeoObject> geoObjects;
        if (mIsStickyModeOn && mStickyBBox != null) {
            geoObjects = server.loadGeoDataWithinBBox(mStickyBBox);
        } else {
            geoObjects = server.extractData();
        }
        if (geoObjects != null) {
            mData.clear();
            mData.addAll(geoObjects);
            zoomToFit();
            repaint();
        }
    }

    /**
     * Loads data from the OSM server.
     */
    public void loadOSMData() {
        OSMServer osmServer = new OSMServer();
        mDrawingContext = osmServer.getOSMDrawingContext();
        mData.clear();
        if (mIsStickyModeOn && mStickyBBox != null) {
            mData.addAll(osmServer.loadOSMDataWithinBBox(mStickyBBox));
        } else {
            mData.addAll(osmServer.loadOSMData());
        }
        zoomToFit();
        repaint();
    }

    /**
     * Loads points of interest (POI) data.
     */
    public void loadPOI() {
        mPois.add((new POIObject("apartment.png", new Point(1615569, 6167042))));
        mPois.add((new POIObject("aquarium.png", new Point(1616258,6166449))));
        mPois.add((new POIObject("art-museum.png", new Point(1617627,6165262))));
        mPois.add((new POIObject("augenarzt.png", new Point(1614780,6165312))));
        mPois.add((new POIObject("badminton.png", new Point(1617110,6165448))));
    }

    /**
     * Toggles the visibility of POIs on the map.
     */
    public void togglePOI() {
        mShowPOIs = !mShowPOIs;
        repaint();
    }

    /**
     * Stores the current image to a file.
     */
    public void storeImg() {
        try {
            mStoredImagesCounter++;
            // Get the current working directory
            String currentDir = System.getProperty("user.dir");

            // Construct the relative path
            String relativePath = "/src/main/resources/storedImages/storedImage_" + mStoredImagesCounter + ".png";

            // Create the file object
            File outputFile = new File(currentDir + relativePath);

            // Write the image to the file
            ImageIO.write(mImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Toggles the sticky mode.
     */
    public void toggleStickyMode() {
        mIsStickyModeOn = !mIsStickyModeOn;
        if (!mIsStickyModeOn) {
            mStickyBBox = null;
        }
    }

    /**
     * Checks if the sticky mode is on.
     * @return True if sticky mode is on, false otherwise.
     */
    public boolean isStickyModeOn() {
        return mIsStickyModeOn;
    }

    /**
     * Sets the sticky bounding box.
     * @param bbox The bounding box to set.
     */
    public void setStickyBBox(Rectangle bbox) {
        var mapPoint1 = getMapPoint(new Point(bbox.x, bbox.y));
        var mapPoint2 = getMapPoint(new Point(bbox.x + bbox.width, bbox.y + bbox.height));
        var rect = new Rectangle(mapPoint1);
        rect.add(mapPoint2);
        mStickyBBox = rect;
    }
}