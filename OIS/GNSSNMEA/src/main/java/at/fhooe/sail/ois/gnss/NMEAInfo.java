package at.fhooe.sail.ois.gnss;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * NMEAInfo is a data class that holds information parsed from NMEA GNSS data sentences.
 * It contains various fields such as latitude, longitude, satellite count, dilution of precision (DOP) values,
 * and a list of satellite information.
 */
@Data
public class NMEAInfo {
    private double mLat;
    private double mLon;
    private String mTime;
    private int mSatelliteCount;
    private double mPDOP;
    private double mHDOP;
    private double mVDOP;
    private String mFixQuality;
    private double mHeight;
    private String mType;
    private Vector<SatelliteInfo> mSatellites = new Vector<>();
    private List<Integer> mUsedSatellites = new ArrayList<>();

    /**
     * Adds a SatelliteInfo object to the list of satellites.
     *
     * @param satelliteInfo The SatelliteInfo object to be added.
     */
    public void addSatellite(SatelliteInfo satelliteInfo) {
        mSatellites.add(satelliteInfo);
    }
}
