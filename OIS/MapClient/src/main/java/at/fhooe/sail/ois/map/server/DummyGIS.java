package at.fhooe.sail.ois.map.server;

import java.awt.Polygon;

import java.util.Vector;

import at.fhooe.sail.ois.map.client.ADrawingContext;
import at.fhooe.sail.ois.map.client.DummyDrawingContext;
import at.fhooe.sail.ois.map.server.feature.Area;
import at.fhooe.sail.ois.map.server.feature.GeoObject;
import at.fhooe.sail.ois.map.server.feature.GeoObjectPart;
import de.intergis.JavaClient.comm.CgGeoConnection;
import de.intergis.JavaClient.comm.CgConnection;
import de.intergis.JavaClient.comm.CgGeoInterface;
import de.intergis.JavaClient.comm.CgResultSet;
import de.intergis.JavaClient.comm.CgStatement;
import de.intergis.JavaClient.comm.CgIGeoObject;
import de.intergis.JavaClient.comm.CgIGeoPart;

import de.intergis.JavaClient.gui.IgcConnection;

/**
 * Represents a dummy GIS (Geographic Information System) that interacts with a Geo-server.
 * This class provides methods to initialize the connection to the server, retrieve the drawing context,
 * and extract geo-objects from the server based on SQL statements.
 */
public class DummyGIS {
    // die Verbindung zum Geo-Server
    CgGeoConnection m_geoConnection = null;
    // das Anfrage-Interface des Geo-Servers
    CgGeoInterface m_geoInterface = null;

    /**
     * Constructor for the DummyGIS class.
     */
    public DummyGIS() {
    }

    /**
     * Initializes the connection to the Geo-server.
     *
     * @return true if the connection was successfully initialized, false otherwise.
     */
    public boolean init() {
        try {
            // der Geo-Server wird initialisiert
            m_geoConnection =
                    new IgcConnection(new CgConnection("admin",
                            "admin",
                            "T:11.11.111.111:4949", // CHANGE THIS! IP-Address of the server : Port
                            null));
            // das Anfrage-Interface des Servers wird abgeholt
            m_geoInterface = m_geoConnection.getInterface();
            return true;
        } catch (Exception _e) {
            _e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves the drawing context.
     *
     * @return the drawing context.
     */
    public ADrawingContext getDrawingContext() {
        return new DummyDrawingContext();
    }

    /**
     * Extracts geo-objects from the server based on a SQL statement.
     *
     * @param _stmt the SQL statement to execute.
     * @return a vector of GeoObject containing the extracted geo-objects, or null if an error occurs.
     */
    public Vector<GeoObject> extractData(String _stmt) {
        try {
            CgStatement stmt = m_geoInterface.Execute(_stmt);
            CgResultSet cursor = stmt.getCursor();
            Vector<GeoObject> objectContainer = new Vector<>();
            while (cursor.next()) {
                CgIGeoObject obj = cursor.getObject();
                CgIGeoPart[] parts = obj.getParts();
                for (int i = 0; i < parts.length; i++) {
                    int pointCount = parts[i].getPointCount();
                    int[] xArray = parts[i].getX();
                    int[] yArray = parts[i].getY();
                    Polygon poly = new Polygon(xArray, yArray, pointCount);
                    Area area = new Area(poly);
                    Vector<GeoObjectPart> geoObjectParts = new Vector<>();
                    geoObjectParts.add(area);
                    GeoObject geoObject = new GeoObject(obj.getName(), Integer.parseInt(String.valueOf(obj.getCategory())), geoObjectParts);
                    objectContainer.addElement(geoObject);
                }
            }
            return objectContainer;
        } catch (Exception _e) {
            _e.printStackTrace();
        }
        return null;
    }

    /**
     * The main method to run the DummyGIS application.
     *
     * @param _argv the command line arguments.
     */
    public static void main(String[] _argv) {
        DummyGIS server = new DummyGIS();
        if (server.init()) {
            Vector objects = server.extractData("select * from data where type = 1101");
        }
    }
}

