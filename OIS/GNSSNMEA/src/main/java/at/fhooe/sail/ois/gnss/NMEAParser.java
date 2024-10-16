package at.fhooe.sail.ois.gnss;

import java.io.IOException;
import java.util.*;

/**
 * NMEAParser is a class that implements the Runnable interface to parse NMEA sentences from a GNSSSimulator.
 * It processes different types of NMEA sentences (GPGGA, GPGSA, GPGSV) and notifies registered listeners
 * of position updates.
 */
public class NMEAParser implements Runnable {

    private GNSSSimulator mSimulator;
    private Thread mParsingThread;
    private Vector<IPositionUpdateListener> mListeners = new Vector<>();
    private NMEAInfo mCurrentInfo = new NMEAInfo();

    /**
     * Constructs an NMEAParser with the specified GNSSSimulator.
     *
     * @param simulator The GNSSSimulator to read NMEA sentences from.
     */
    public NMEAParser(GNSSSimulator simulator) {
        this.mSimulator = simulator;
    }

    /**
     * The main run method of the parser thread. It reads lines from the GNSSSimulator,
     * processes them based on their type, and notifies listeners of updates.
     */
    @Override
    public void run() {
        try {
            String line;
            while ((line = mSimulator.readLine()) != null) {
                boolean updated = false;

                if (validateChecksum(line)) {
                    if (line.startsWith("$GPGGA")) {
                        mCurrentInfo = new NMEAInfo();  // Reset currentInfo for each new GGA message
                        processGPGGA(line);
                        updated = true;
                    } else if (line.startsWith("$GPGSA")) {
                        processGPGSA(line);
                        updated = true;
                    } else if (line.startsWith("$GPGSV")) {
                        processGPGSV(line);
                        updated = true;
                    }
                }

                if (updated) {
                    notifyListeners();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes a GPGGA sentence and updates the current NMEAInfo object with the extracted data.
     *
     * @param line The GPGGA sentence to process.
     */
    private void processGPGGA(String line) {
        String[] split = line.split(",");
        if (split.length < 10) {
            System.err.println("GGA line too short, skipping: " + line);
            return;
        }

        String time = split[1];
        String latitude = split[2];
        String latitudeDirection = split[3];
        String longitude = split[4];
        String longitudeDirection = split[5];
        String fixQuality = !split[6].isEmpty() ? split[6] : "0";
        int satellites = !split[7].isEmpty() ? Integer.parseInt(split[7]) : 0;
        double hdop = !split[8].isEmpty() ? Double.parseDouble(split[8]) : 0.0;
        double height = !split[9].isEmpty() ? Double.parseDouble(split[9]) : 0.0;

        double latitudeDecimal = convertToDecimalDegrees(latitude, latitudeDirection, true);
        double longitudeDecimal = convertToDecimalDegrees(longitude, longitudeDirection, false);

        mCurrentInfo.setMTime(time);
        mCurrentInfo.setMLat(latitudeDecimal);
        mCurrentInfo.setMLon(longitudeDecimal);
        mCurrentInfo.setMFixQuality(fixQuality);
        mCurrentInfo.setMSatelliteCount(satellites);
        mCurrentInfo.setMHDOP(hdop);
        mCurrentInfo.setMHeight(height);
        mCurrentInfo.setMType("GPGGA");

        System.out.println(mCurrentInfo);
    }

    /**
     * Processes a GPGSA sentence and updates the current NMEAInfo object with the extracted data.
     *
     * @param line The GPGSA sentence to process.
     */
    private void processGPGSA(String line) {
        String[] split = line.split(",");
        if (split.length < 18) {
            System.err.println("GSA line too short, skipping: " + line);
            return;
        }

        List<Integer> usedSatellites = new ArrayList<>();
        for (int i = 3; i <= 14; i++) {
            if (!split[i].isEmpty()) {
                usedSatellites.add(Integer.parseInt(split[i]));
            }
        }

        double pdop = !split[15].isEmpty() ? Double.parseDouble(split[15]) : 0.0;
        double hdop = !split[16].isEmpty() ? Double.parseDouble(split[16]) : 0.0;
        double vdop = !split[17].split("\\*")[0].isEmpty() ? Double.parseDouble(split[17].split("\\*")[0]) : 0.0;

        mCurrentInfo.setMPDOP(pdop);
        mCurrentInfo.setMHDOP(hdop);
        mCurrentInfo.setMVDOP(vdop);
        mCurrentInfo.setMUsedSatellites(usedSatellites);
        mCurrentInfo.setMType("GPGSA");
        System.out.println(mCurrentInfo);
    }

    /**
     * Processes a GPGSV sentence and updates the current NMEAInfo object with the extracted data.
     *
     * @param line The GPGSV sentence to process.
     */
    private void processGPGSV(String line) {
        String[] split = line.split(",");
        if (split.length < 4) {
            System.err.println("GSV line too short, skipping: " + line);
            return;
        }

        int satellitesInView = Integer.parseInt(split[3]);
        mCurrentInfo.setMSatelliteCount(satellitesInView);

        for (int i = 4; i + 3 < split.length; i += 4) {
            try {
                String satelliteIdStr = split[i];
                String elevationStr = split[i + 1];
                String azimuthStr = split[i + 2];
                String snrStr = split[i + 3].contains("*") ? split[i + 3].split("\\*")[0] : split[i + 3];

                SatelliteInfo satelliteInfo = new SatelliteInfo();

                if (!satelliteIdStr.isEmpty()) {
                    satelliteInfo.setMId(Integer.parseInt(satelliteIdStr));
                }

                if (!elevationStr.isEmpty()) {
                    satelliteInfo.setMVerticalAngle(Double.parseDouble(elevationStr));
                } else {
                    satelliteInfo.setMVerticalAngle(0); // Set default value
                }

                if (!azimuthStr.isEmpty()) {
                    satelliteInfo.setMHorizontalAngle(Double.parseDouble(azimuthStr));
                } else {
                    satelliteInfo.setMHorizontalAngle(0); // Set default value
                }

                if (!snrStr.isEmpty()) {
                    satelliteInfo.setMSNR(Double.parseDouble(snrStr));
                } else {
                    satelliteInfo.setMSNR(0); // Set default value
                }

                satelliteInfo.setMIsUsed(mCurrentInfo.getMUsedSatellites().contains(satelliteInfo.getMId()));
                satelliteInfo.setMType("GPGSV");
                mCurrentInfo.addSatellite(satelliteInfo);
                System.out.println(satelliteInfo);

            } catch (NumberFormatException e) {
                System.err.println("Skipping invalid GSV data: " + line);
            }
        }
    }

    /**
     * Validates the checksum of an NMEA sentence.
     *
     * @param line the NMEA sentence to validate
     * @return true if the checksum is valid, false otherwise
     */
    private boolean validateChecksum(String line) {
        int checksumIndex = line.indexOf("*");
        if (checksumIndex != -1) {
            String checksum = line.substring(checksumIndex + 1);
            line = line.substring(1, checksumIndex);
            int checksumValue = Integer.parseInt(checksum, 16);
            int computedChecksum = 0;
            for (int i = 0; i < line.length(); i++) {
                computedChecksum ^= line.charAt(i);
            }
            if (computedChecksum == checksumValue) {
                System.out.println("Valid checksum: " + line);
                return true;
            } else {
                System.out.println("Invalid checksum: " + line);
                return false;
            }
        } else {
            System.out.println("No checksum: " + line);
            return false;
        }
    }

    /**
     * Converts a coordinate in NMEA format to decimal degrees.
     *
     * @param coordinate The coordinate in NMEA format.
     * @param direction The direction (N, S, E, W) of the coordinate.
     * @param isLatitude True if the coordinate is a latitude, false if it is a longitude.
     * @return The coordinate in decimal degrees.
     */
    private double convertToDecimalDegrees(String coordinate, String direction, boolean isLatitude) {
        if (coordinate.isEmpty() || direction.isEmpty()) {
            return 0.0;
        }

        try {
            double degrees;
            double minutes;

            if (isLatitude) {
                degrees = Double.parseDouble(coordinate.substring(0, 2));
                minutes = Double.parseDouble(coordinate.substring(2));
            } else {
                degrees = Double.parseDouble(coordinate.substring(0, 3));
                minutes = Double.parseDouble(coordinate.substring(3));
            }

            double decimal = degrees + (minutes / 60.0);

            if (direction.equals("S") || direction.equals("W")) {
                decimal = -decimal;
            }

            return decimal;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            System.err.println("Error: " + coordinate + " Direction: " + direction);
            return 0.0;
        }
    }

    /**
     * Starts the parser thread to begin reading and processing NMEA sentences.
     */
    public void start() {
        mParsingThread = new Thread(this);
        mParsingThread.start();
    }

    /**
     * Adds a listener to be notified of position updates.
     *
     * @param listener The listener to add.
     */
    public void addPositionUpdateListener(IPositionUpdateListener listener) {
        mListeners.add(listener);
    }

    /**
     * Notifies all registered listeners of a position update.
     */
    private void notifyListeners() {
        for (IPositionUpdateListener listener : mListeners) {
            listener.update(mCurrentInfo);
        }
    }
}