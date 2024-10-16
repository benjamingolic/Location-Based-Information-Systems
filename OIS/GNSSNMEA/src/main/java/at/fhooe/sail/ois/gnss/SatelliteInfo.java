package at.fhooe.sail.ois.gnss;

import lombok.Data;

/**
 * SatelliteInfo is a data class that holds information about a GNSS satellite.
 * It contains fields such as satellite ID, vertical angle, horizontal angle, signal-to-noise ratio (SNR),
 * type of NMEA sentence, and whether the satellite is used in the current solution.
 */
@Data
public class SatelliteInfo {
    private int mId;
    private double mVerticalAngle;
    private double mHorizontalAngle;
    private double mSNR;
    private String mType;
    private boolean mIsUsed;
}
