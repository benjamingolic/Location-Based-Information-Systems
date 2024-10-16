package at.fhooe.sail.ois.map.client;

import javafx.scene.paint.Color;

/**
 * A concrete implementation of the abstract class ADrawingContext for OpenStreetMap (OSM) data.
 * This class initializes a set of predefined presentation schemas for various OSM feature types.
 * Each schema defines the fill color, outline color, and stroke width for rendering the corresponding feature types.
 */
public class OSMDrawingContext extends ADrawingContext {

    /**
     * Initializes the presentation schemas for OSM data.
     * This method populates the mContext hashtable with predefined presentation schemas for various feature types
     * including highways, waterways, railways, leisure areas, land use areas, natural features, places, boundaries, buildings, and amenities.
     */
    @Override
    protected void initSchemata() {
        // Highways
        mContext.put(1010, new PresentationSchema(Color.RED, null, 2f)); // Autobahn
        mContext.put(1011, new PresentationSchema(Color.RED, null, 1.5f)); // Autobahn Auffahrt
        mContext.put(1020, new PresentationSchema(Color.ORANGE, null, 2f)); // Schnellstraße
        mContext.put(1021, new PresentationSchema(Color.ORANGE, null, 1.5f)); // Schnellstraßen Auffahrt
        mContext.put(1030, new PresentationSchema(Color.YELLOW, null, 2f)); // Bundesstraße
        mContext.put(1031, new PresentationSchema(Color.YELLOW, null, 1.5f)); // Bundesstraßen Auffahrt
        mContext.put(1040, new PresentationSchema(Color.LIGHTYELLOW, null, 2f)); // Landesstraße
        mContext.put(1041, new PresentationSchema(Color.LIGHTYELLOW, null, 1.5f)); // Landesstraßen Auffahrt
        mContext.put(1050, new PresentationSchema(Color.LIGHTGREEN, null, 2f)); // Kreisstraße
        mContext.put(1051, new PresentationSchema(Color.LIGHTGREEN, null, 1.5f)); // Kreisstraßen Auffahrt
        mContext.put(1060, new PresentationSchema(Color.DARKGREY, null, 2f)); // Anliegerstraße
        mContext.put(1070, new PresentationSchema(Color.LIGHTGREY, null, 2f)); // Fußgängerzone
        mContext.put(1080, new PresentationSchema(Color.LIGHTGREY, null, 2f)); // Wohnstraße
        mContext.put(1090, new PresentationSchema(Color.WHITE, null, 1f)); // Unklassifizierte Straße
        mContext.put(1091, new PresentationSchema(Color.WHITE, null, 1f)); // Straße
        mContext.put(1100, new PresentationSchema(Color.WHITE, null, 1f)); // Servicestelle
        mContext.put(1110, new PresentationSchema(Color.BROWN, null, 1f)); // Feldweg
        mContext.put(1120, new PresentationSchema(Color.BROWN, null, 1f)); // Pfad
        mContext.put(1130, new PresentationSchema(Color.BROWN, null, 1f)); // Gehweg

        // Waterways
        mContext.put(2001, new PresentationSchema(Color.BLUE, null, 1f)); // Fluss
        mContext.put(2002, new PresentationSchema(Color.DEEPSKYBLUE, null, 1f)); // Kanal
        mContext.put(2003, new PresentationSchema(Color.CADETBLUE, null, 1f)); // Graben
        mContext.put(2004, new PresentationSchema(Color.LIGHTSKYBLUE, null, 1f)); // Bach
        mContext.put(2005, new PresentationSchema(Color.DARKBLUE, null, 1f)); // Entwässerungsgraben
        mContext.put(2006, new PresentationSchema(Color.CORNFLOWERBLUE, Color.CORNFLOWERBLUE, 1f)); // Flussufer

        // Railways
        mContext.put(3001, new PresentationSchema(Color.BLACK, null, 1f)); // Eisenbahn
        mContext.put(3002, new PresentationSchema(Color.DIMGRAY, null, 1f)); // U-Bahn
        mContext.put(3003, new PresentationSchema(Color.DIMGRAY, null, 1f)); // Straßenbahn
        mContext.put(3004, new PresentationSchema(Color.DIMGRAY, null, 1f)); // Leichtbahn

        // Leisure
        mContext.put(4001, new PresentationSchema(Color.FORESTGREEN, Color.FORESTGREEN, 1f)); // Naturschutzgebiet
        mContext.put(4002, new PresentationSchema(Color.GREEN, Color.GREEN, 1f)); // Park
        mContext.put(4004, new PresentationSchema(Color.YELLOWGREEN, Color.YELLOWGREEN, 1f)); // Spielplatz
        mContext.put(4005, new PresentationSchema(Color.MEDIUMSEAGREEN, Color.MEDIUMSEAGREEN, 1f)); // Garten
        mContext.put(4006, new PresentationSchema(Color.LIMEGREEN, Color.LIMEGREEN, 1f)); // Spielfeld
        mContext.put(4007, new PresentationSchema(Color.DARKGREEN, Color.DARKGREEN, 1f)); // Stadion
        mContext.put(4008, new PresentationSchema(Color.TURQUOISE, Color.TURQUOISE, 1f)); // Schwimmbad
        mContext.put(4009, new PresentationSchema(Color.LIGHTSEAGREEN, Color.LIGHTSEAGREEN, 1f)); // Wasserpark

        // Landuse
        mContext.put(5001, new PresentationSchema(Color.LIGHTGRAY, Color.LIGHTGRAY, 1f)); // Wohngebiet
        mContext.put(5002, new PresentationSchema(Color.DARKGRAY, Color.DARKGRAY, 1f)); // Industriegebiet
        mContext.put(5003, new PresentationSchema(Color.DARKGRAY, Color.DARKGRAY, 1f)); // Gewerbegebiet
        mContext.put(5004, new PresentationSchema(Color.DARKGREEN, Color.DARKGREEN, 1f)); // Wald
        mContext.put(5005, new PresentationSchema(Color.LAWNGREEN, Color.LAWNGREEN, 1f)); // Gras
        mContext.put(5006, new PresentationSchema(Color.DARKKHAKI, Color.DARKKHAKI, 1f)); // Wiese
        mContext.put(5007, new PresentationSchema(Color.SADDLEBROWN, Color.SADDLEBROWN, 1f)); // Landwirtschaft
        mContext.put(5008, new PresentationSchema(Color.SIENNA, Color.SIENNA, 1f)); // Ackerland
        mContext.put(5009, new PresentationSchema(Color.PERU, Color.PERU, 1f)); // Hof
        mContext.put(5010, new PresentationSchema(Color.LIGHTGOLDENRODYELLOW, Color.LIGHTGOLDENRODYELLOW, 1f)); // Kleingärten
        mContext.put(5011, new PresentationSchema(Color.DARKRED, Color.DARKRED, 1f)); // Friedhof
        mContext.put(5012, new PresentationSchema(Color.LIGHTGREEN, Color.LIGHTGREEN, 1f)); // Freizeitanlage
        mContext.put(5013, new PresentationSchema(Color.DEEPSKYBLUE, Color.DEEPSKYBLUE, 1f)); // Stausee
        mContext.put(5014, new PresentationSchema(Color.DARKOLIVEGREEN, Color.DARKOLIVEGREEN, 1f)); // Weinberg
        mContext.put(5015, new PresentationSchema(Color.SKYBLUE, Color.SKYBLUE, 1f)); // Becken
        mContext.put(5016, new PresentationSchema(Color.DARKGRAY, Color.DARKGRAY, 1f)); // Einzelhandel
        mContext.put(5017, new PresentationSchema(Color.MEDIUMSEAGREEN, Color.MEDIUMSEAGREEN, 1f)); // Dorfplatz
        mContext.put(5018, new PresentationSchema(Color.BLACK, null, 1f)); // Eisenbahn
        mContext.put(5019, new PresentationSchema(Color.WHEAT, Color.WHEAT, 1f)); // Ackerbau

        // Natural
        mContext.put(6001, new PresentationSchema(Color.LIGHTGREEN, Color.LIGHTGREEN, 1f)); // Grünland
        mContext.put(6002, new PresentationSchema(Color.DARKGREEN, Color.DARKGREEN, 1f)); // Wald
        mContext.put(6003, new PresentationSchema(Color.DARKSEAGREEN, Color.DARKSEAGREEN, 1f)); // Gestrüpp
        mContext.put(6004, new PresentationSchema(Color.OLIVEDRAB, Color.OLIVEDRAB, 1f)); // Felsen
        mContext.put(6005, new PresentationSchema(Color.SKYBLUE, Color.SKYBLUE, 1f)); // Wasser
        mContext.put(6006, new PresentationSchema(Color.BEIGE, Color.BEIGE, 1f)); // Land
        mContext.put(6007, new PresentationSchema(Color.GRAY, Color.GRAY, 1f)); // Fels
        mContext.put(6008, new PresentationSchema(Color.SANDYBROWN, Color.SANDYBROWN, 1f)); // Strand
        mContext.put(6009, new PresentationSchema(Color.DARKSLATEGRAY, Color.DARKSLATEGRAY, 1f)); // Küstenlinie
        mContext.put(6010, new PresentationSchema(Color.DARKSEAGREEN, Color.DARKSEAGREEN, 1f)); // Moor
        mContext.put(6011, new PresentationSchema(Color.TURQUOISE, Color.TURQUOISE, 1f)); // Gletscher
        mContext.put(6012, new PresentationSchema(Color.LIGHTSKYBLUE, Color.LIGHTSKYBLUE, 1f)); // Sumpf
        mContext.put(6013, new PresentationSchema(Color.DARKSLATEGRAY, Color.DARKSLATEGRAY, 1f)); // Klippe
        mContext.put(6014, new PresentationSchema(Color.CYAN, null, 1f)); // Baum
        mContext.put(6015, new PresentationSchema(Color.DARKSLATEGRAY, Color.DARKSLATEGRAY, 1f)); // Gipfel

        // Places
        mContext.put(7001, new PresentationSchema(Color.MEDIUMBLUE, null, 1f)); // Stadt
        mContext.put(7002, new PresentationSchema(Color.DODGERBLUE, null, 1f)); // Stadt
        mContext.put(7003, new PresentationSchema(Color.CORNFLOWERBLUE, null, 1f)); // Dorf
        mContext.put(7004, new PresentationSchema(Color.LIGHTSTEELBLUE, null, 1f)); // Weiler
        mContext.put(7005, new PresentationSchema(Color.AQUA, null, 1f)); // Insel
        mContext.put(7006, new PresentationSchema(Color.AQUAMARINE, null, 1f)); // Inselchen
        mContext.put(7007, new PresentationSchema(Color.PALETURQUOISE, null, 1f)); // Lokalität
        mContext.put(7008, new PresentationSchema(Color.TEAL, null, 1f)); // Einsiedlerhof

        // Boundaries
        mContext.put(8001, new PresentationSchema(Color.BLACK, null, 1.5f)); // Verwaltungsgrenze Ebene 2
        mContext.put(8002, new PresentationSchema(Color.DIMGRAY, null, 1.5f)); // Verwaltungsgrenze Ebene 4
        mContext.put(8003, new PresentationSchema(Color.GRAY, null, 1.5f)); // Verwaltungsgrenze Ebene 5
        mContext.put(8004, new PresentationSchema(Color.DARKGRAY, null, 1.5f)); // Verwaltungsgrenze Ebene 6
        mContext.put(8005, new PresentationSchema(Color.SLATEGRAY, null, 1.5f)); // Verwaltungsgrenze Ebene 8
        mContext.put(8006, new PresentationSchema(Color.LIGHTSLATEGRAY, null, 1.5f)); // Verwaltungsgrenze Ebene 9
        mContext.put(8007, new PresentationSchema(Color.LIGHTGRAY, null, 1.5f)); // Verwaltungsgrenze Ebene 10

        // Buildings
        mContext.put(9001, new PresentationSchema(Color.LIGHTGRAY, null, 1f)); // Wohngebäude
        mContext.put(9002, new PresentationSchema(Color.GRAY, null, 1f)); // Wohnungen
        mContext.put(9003, new PresentationSchema(Color.DARKGRAY, null, 1f)); // Haus
        mContext.put(9004, new PresentationSchema(Color.GREEN, null, 1f)); // Gewächshaus
        mContext.put(9005, new PresentationSchema(Color.YELLOW, null, 1f)); // Schule
        mContext.put(9006, new PresentationSchema(Color.SILVER, null, 1f)); // Büro
        mContext.put(9007, new PresentationSchema(Color.DIMGRAY, null, 1f)); // Industrie
        mContext.put(9008, new PresentationSchema(Color.LIGHTYELLOW, null, 1f)); // Kirche
        mContext.put(9009, new PresentationSchema(Color.WHITESMOKE, null, 1f)); // Lager
        mContext.put(9010, new PresentationSchema(Color.LIGHTCYAN, null, 1f)); // Supermarkt
        mContext.put(9011, new PresentationSchema(Color.LIGHTPINK, null, 1f)); // Öffentliches Gebäude
        mContext.put(9012, new PresentationSchema(Color.RED, null, 1f)); // Krankenhaus
        mContext.put(9013, new PresentationSchema(Color.LIGHTGREEN, null, 1f)); // Kloster
        mContext.put(9014, new PresentationSchema(Color.SADDLEBROWN, null, 1f)); // Bauernhof
        mContext.put(9015, new PresentationSchema(Color.DARKSEAGREEN, null, 1f)); // Ziviles Gebäude
        mContext.put(9016, new PresentationSchema(Color.LIGHTSALMON, null, 1f)); // Schloss
        mContext.put(9017, new PresentationSchema(Color.INDIANRED, null, 1f)); // Universität
        mContext.put(9018, new PresentationSchema(Color.DARKOLIVEGREEN, null, 1f)); // Stall
        mContext.put(9019, new PresentationSchema(Color.DIMGRAY, null, 1f)); // Ruinen
        mContext.put(9020, new PresentationSchema(Color.GAINSBORO, null, 1f)); // Garage
        mContext.put(9021, new PresentationSchema(Color.GAINSBORO, null, 1f)); // Garagen
        mContext.put(9022, new PresentationSchema(Color.PERU, null, 1f)); // Hütte
        mContext.put(9023, new PresentationSchema(Color.BURLYWOOD, null, 1f)); // Dach
        mContext.put(9024, new PresentationSchema(Color.DARKKHAKI, null, 1f)); // Einfamilienhaus
        mContext.put(9025, new PresentationSchema(Color.KHAKI, null, 1f)); // Schuppen
        mContext.put(9026, new PresentationSchema(Color.SLATEGRAY, null, 1f)); // Gewerbe
        mContext.put(9027, new PresentationSchema(Color.TAN, null, 1f)); // Terrasse
        mContext.put(9028, new PresentationSchema(Color.DARKSLATEGRAY, null, 1f)); // Einzelhandel

        // Amenities
        mContext.put(10001, new PresentationSchema(Color.RED, null, 1f)); // Krankenhaus
        mContext.put(10002, new PresentationSchema(Color.LIGHTSKYBLUE, null, 1f)); // Apotheke
        mContext.put(10003, new PresentationSchema(Color.DARKBLUE, null, 1f)); // Polizei
        mContext.put(10004, new PresentationSchema(Color.FIREBRICK, null, 1f)); // Feuerwehr
        mContext.put(10005, new PresentationSchema(Color.DARKORANGE, null, 1f)); // Tankstelle
        mContext.put(10006, new PresentationSchema(Color.GOLD, null, 1f)); // Schnellimbiss
        mContext.put(10007, new PresentationSchema(Color.MEDIUMSLATEBLUE, null, 1f)); // Bank
        mContext.put(10008, new PresentationSchema(Color.DARKRED, null, 1f)); // Universität
        mContext.put(10009, new PresentationSchema(Color.DARKRED, null, 1f)); // Hochschule
        mContext.put(10010, new PresentationSchema(Color.DARKKHAKI, null, 1f)); // Schule
        mContext.put(10011, new PresentationSchema(Color.DARKKHAKI, null, 1f)); // Kindergarten
        mContext.put(10012, new PresentationSchema(Color.LIGHTYELLOW, null, 1f)); // Gebetshaus
        mContext.put(10013, new PresentationSchema(Color.SILVER, null, 1f)); // Parkplatz
        mContext.put(10014, new PresentationSchema(Color.TURQUOISE, null, 1f)); // Schwimmbad
        mContext.put(10015, new PresentationSchema(Color.PALEVIOLETRED, null, 1f)); // Restaurant
        mContext.put(10016, new PresentationSchema(Color.SILVER, null, 1f)); // Parklücke
        mContext.put(10017, new PresentationSchema(Color.DARKRED, null, 1f)); // Friedhof
        mContext.put(10018, new PresentationSchema(Color.DIMGRAY, null, 1f)); // Gefängnis
        mContext.put(10019, new PresentationSchema(Color.BURLYWOOD, null, 1f)); // Bank
        mContext.put(10020, new PresentationSchema(Color.PINK, null, 1f)); // Café
        mContext.put(10021, new PresentationSchema(Color.CORAL, null, 1f)); // Briefkasten
        mContext.put(10022, new PresentationSchema(Color.SALMON, null, 1f)); // Postamt
        mContext.put(10023, new PresentationSchema(Color.DARKSEAGREEN, null, 1f)); // Recycling
        mContext.put(10024, new PresentationSchema(Color.LIGHTYELLOW, null, 1f)); // Toiletten
        mContext.put(10025, new PresentationSchema(Color.KHAKI, null, 1f)); // Unterstand
        mContext.put(10026, new PresentationSchema(Color.ROSYBROWN, null, 1f)); // Lagerung
    }
}
