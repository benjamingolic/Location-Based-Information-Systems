package at.fhooe.sail.ois.map.server;

import at.fhooe.sail.ois.map.server.feature.GeoObject;
import at.fhooe.sail.ois.map.client.VerwaltungsgrenzenDrawingContext;
import at.fhooe.sail.ois.map.server.feature.GeoObjectPart;
import at.fhooe.sail.ois.map.server.feature.Line;
import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Geometry;
import org.postgresql.PGConnection;

import java.awt.*;
import java.sql.*;
import java.util.*;

public class VerwaltungsgrenzenServer {

    VerwaltungsgrenzenDrawingContext mVerwaltungsgrenzenDrawingContext = new VerwaltungsgrenzenDrawingContext();

    private Connection mConnection;
    private String mUrl = "jdbc:postgresql://localhost:5432/osm_verwaltungsgrenzen_3857";
    private String mUser = "admin";
    private String mPassword = "admin";

    /**
     * Constructs a VerwaltungsgrenzenServer object and initializes the database connection.
     */
    public VerwaltungsgrenzenServer() {
        initializeConnection();
    }

    /**
     * Initializes the connection to the PostgreSQL database.
     */
    private void initializeConnection() {
        try {
            Class.forName("net.postgis.jdbc.DriverWrapper");
            mConnection = DriverManager.getConnection(mUrl, mUser, mPassword);
            PGConnection c = (PGConnection) mConnection;
            c.addDataType("geometry", net.postgis.jdbc.PGgeometry.class);
            c.addDataType("box2d", net.postgis.jdbc.PGbox2d.class);
        } catch (Exception e) {
            handleError(e);
        }
    }

    /**
     * Extracts administrative boundary data from the database.
     *
     * @return a vector of GeoObject containing the extracted data.
     */
    public Vector<GeoObject> extractData() {
        return executeQuery("SELECT * FROM bundeslaender AS a WHERE a.type IN (8001, 8002, 8004);");
    }

    /**
     * Loads geographic data within a specified bounding box.
     *
     * @param bbox the bounding box within which to load the data.
     * @return a vector of GeoObject containing the loaded data.
     */
    public Vector<GeoObject> loadGeoDataWithinBBox(Rectangle bbox) {
        String query = getQueryForBBox(bbox);
        return executeQuery(query);
    }

    /**
     * Constructs a SQL query for selecting data within a bounding box.
     *
     * @param bbox the bounding box within which to select the data.
     * @return the SQL query string.
     */
    private String getQueryForBBox(Rectangle bbox) {
        double minX = bbox.getX();
        double minY = bbox.getY();
        double maxX = bbox.getX() + bbox.getWidth();
        double maxY = bbox.getY() + bbox.getHeight();
        return "SELECT * FROM bundeslaender WHERE ST_Intersects(geom, ST_GeomFromText('POLYGON((" +
                minX + " " + minY + ", " +
                minX + " " + maxY + ", " +
                maxX + " " + maxY + ", " +
                maxX + " " + minY + ", " +
                minX + " " + minY + "))', 0));";
    }

    /**
     * Executes a SQL query and processes the result set.
     *
     * @param query the SQL query to execute.
     * @return a vector of GeoObject containing the query results.
     */
    private Vector<GeoObject> executeQuery(String query) {
        Vector<GeoObject> geoContainer = new Vector<>();
        try (Statement s = mConnection.createStatement();
             ResultSet r = s.executeQuery(query)) {
            while (r.next()) {
                processResultSetRow(r, geoContainer);
            }
        } catch (Exception e) {
            handleError(e);
        } finally {
            closeConnection();
        }
        return geoContainer;
    }

    /**
     * Processes a row from the result set and adds the corresponding GeoObject to the container.
     *
     * @param r the result set containing the data.
     * @param geoContainer the container to which the GeoObject is added.
     * @throws SQLException if an SQL error occurs.
     */
    private void processResultSetRow(ResultSet r, Vector<GeoObject> geoContainer) throws SQLException {
        String id = r.getString("id");
        int type = r.getInt("type");
        PGgeometry geom = (PGgeometry) r.getObject("geom");

        switch (geom.getGeoType()) {
            case Geometry.POLYGON:
                processPolygon(id, type, geom, geoContainer);
                break;
            case Geometry.MULTIPOLYGON:
                processMultiPolygon(id, type, geom, geoContainer);
                break;
            case Geometry.LINESTRING:
                processLineString(id, type, geom, geoContainer);
                break;
            case Geometry.POINT:
                processPoint(id, type, geom, geoContainer);
                break;
            case Geometry.MULTILINESTRING:
                processMultiLineString(id, type, geom, geoContainer);
                break;
        }
    }

    /**
     * Processes a polygon geometry and adds the corresponding GeoObject to the container.
     *
     * @param id the ID of the geometry.
     * @param type the type of the geometry.
     * @param geom the geometry object.
     * @param geoContainer the container to which the GeoObject is added.
     * @throws SQLException if an SQL error occurs.
     */
    private void processPolygon(String id, int type, PGgeometry geom, Vector<GeoObject> geoContainer) throws SQLException {
        String wkt = geom.toString();
        net.postgis.jdbc.geometry.Polygon p = new net.postgis.jdbc.geometry.Polygon(wkt);
        if (p.numRings() >= 1) {
            geoContainer.add(createGeoObjectFromPolygon(id, type, p));
        }
    }

    /**
     * Processes a multipolygon geometry and adds the corresponding GeoObject to the container.
     *
     * @param id the ID of the geometry.
     * @param type the type of the geometry.
     * @param geom the geometry object.
     * @param geoContainer the container to which the GeoObject is added.
     * @throws SQLException if an SQL error occurs.
     */
    private void processMultiPolygon(String id, int type, PGgeometry geom, Vector<GeoObject> geoContainer) throws SQLException {
        String wkt = geom.toString();
        net.postgis.jdbc.geometry.MultiPolygon mp = new net.postgis.jdbc.geometry.MultiPolygon(wkt);
        for (int i = 0; i < mp.numPolygons(); i++) {
            net.postgis.jdbc.geometry.Polygon p = mp.getPolygon(i);
            if (p.numRings() >= 1) {
                geoContainer.add(createGeoObjectFromPolygon(id, type, p));
            }
        }
    }

    /**
     * Processes a linestring geometry and adds the corresponding GeoObject to the container.
     *
     * @param id the ID of the geometry.
     * @param type the type of the geometry.
     * @param geom the geometry object.
     * @param geoContainer the container to which the GeoObject is added.
     */
    private void processLineString(String id, int type, PGgeometry geom, Vector<GeoObject> geoContainer) {
        net.postgis.jdbc.geometry.LineString lineString = (net.postgis.jdbc.geometry.LineString) geom.getGeometry();
        geoContainer.add(createGeoObjectFromLineString(id, type, lineString));
    }

    /**
     * Processes a point geometry and adds the corresponding GeoObject to the container.
     *
     * @param id the ID of the geometry.
     * @param type the type of the geometry.
     * @param geom the geometry object.
     * @param geoContainer the container to which the GeoObject is added.
     */
    private void processPoint(String id, int type, PGgeometry geom, Vector<GeoObject> geoContainer) {
        Vector<GeoObjectPart> geoObjectParts = new Vector<>();
        net.postgis.jdbc.geometry.Point point = geom.getGeometry().getFirstPoint();
        geoObjectParts.add(new at.fhooe.sail.ois.map.server.feature.Point(new java.awt.Point((int) point.x, (int) point.y)));
        geoContainer.add(new GeoObject(id, type, geoObjectParts));
    }

    /**
     * Processes a multilinestring geometry and adds the corresponding GeoObject to the container.
     *
     * @param id the ID of the geometry.
     * @param type the type of the geometry.
     * @param geom the geometry object.
     * @param geoContainer the container to which the GeoObject is added.
     */
    private void processMultiLineString(String id, int type, PGgeometry geom, Vector<GeoObject> geoContainer) {
        net.postgis.jdbc.geometry.MultiLineString multiLineString = (net.postgis.jdbc.geometry.MultiLineString) geom.getGeometry();
        for (int i = 0; i < multiLineString.numLines(); i++) {
            net.postgis.jdbc.geometry.LineString lineString = multiLineString.getLine(i);
            geoContainer.add(createGeoObjectFromLineString(id, type, lineString));
        }
    }

    /**
     * Creates a GeoObject from a polygon geometry.
     *
     * @param id the ID of the geometry.
     * @param type the type of the geometry.
     * @param p the polygon geometry.
     * @return the created GeoObject.
     */
    private GeoObject createGeoObjectFromPolygon(String id, int type, net.postgis.jdbc.geometry.Polygon p) {
        java.awt.Polygon poly = new java.awt.Polygon();
        net.postgis.jdbc.geometry.LinearRing ring = p.getRing(0);
        for (int i = 0; i < ring.numPoints(); i++) {
            net.postgis.jdbc.geometry.Point pPG = ring.getPoint(i);
            double[] xy = transformData(pPG.x, pPG.y);
            poly.addPoint((int) xy[0], (int) xy[1]);
        }
        GeoObjectPart geoObjectPart = new at.fhooe.sail.ois.map.server.feature.Area(poly);
        for (int i = 1; i < p.numRings(); i++) {
            Polygon holePoly = new Polygon();
            ring = p.getRing(i);
            for (int j = 0; j < ring.numPoints(); j++) {
                net.postgis.jdbc.geometry.Point pPG = ring.getPoint(j);
                holePoly.addPoint((int) pPG.x, (int) pPG.y);
            }
            geoObjectPart.addHole(holePoly);
        }
        Vector<GeoObjectPart> geoObjectParts = new Vector<>();
        geoObjectParts.add(geoObjectPart);
        return new GeoObject(id, type, geoObjectParts);
    }

    /**
     * Creates a GeoObject from a linestring geometry.
     *
     * @param id the ID of the geometry.
     * @param type the type of the geometry.
     * @param lineString the linestring geometry.
     * @return the created GeoObject.
     */
    private GeoObject createGeoObjectFromLineString(String id, int type, net.postgis.jdbc.geometry.LineString lineString) {
        Vector<Point> points = new Vector<>();
        Vector<GeoObjectPart> geoObjectParts = new Vector<>();
        for (int i = 0; i < lineString.numPoints(); i++) {
            net.postgis.jdbc.geometry.Point pPG = lineString.getPoint(i);
            double[] xy = transformData(pPG.x, pPG.y);
            points.add(new Point((int) xy[0], (int) xy[1]));
        }
        Line line = new Line(points);
        geoObjectParts.add(line);
        return new GeoObject(id, type, geoObjectParts);
    }

    /*
    * für osm_verwaltungsgrenzen_4326
    private double[] transformData(double _lon, double _lat) {
        double x = _lon * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + _lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        return new double[]{x, y};
    }
    */

    /**
     * Transforms coordinates from longitude and latitude to a different coordinate system.
     *
     * @param _lon the longitude.
     * @param _lat the latitude.
     * @return the transformed coordinates.
     */
    private double[] transformData(double _lon, double _lat) {
        double x = _lon;
        double y = _lat;
        return new double[]{x, y};
    }


    /**
     * Handles errors by printing the stack trace.
     *
     * @param e the exception to handle.
     */
    private void handleError(Exception e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Closes the database connection.
     */
    private void closeConnection() {
        try {
            if (mConnection != null && !mConnection.isClosed()) {
                mConnection.close();
            }
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Retrieves the drawing context for Verwaltungsgrenzen.
     *
     * @return the VerwaltungsgrenzenDrawingContext.
     */
    public VerwaltungsgrenzenDrawingContext getVerwaltungsgrenzenDrawingContext() {
        if (mVerwaltungsgrenzenDrawingContext == null) {
            mVerwaltungsgrenzenDrawingContext = new VerwaltungsgrenzenDrawingContext();
        }
        return mVerwaltungsgrenzenDrawingContext;
    }
}
