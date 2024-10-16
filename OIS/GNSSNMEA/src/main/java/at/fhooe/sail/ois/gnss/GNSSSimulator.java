package at.fhooe.sail.ois.gnss;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * GNSSSimulator extends BufferedReader to simulate reading GNSS data from a file.
 * It introduces a delay between lines containing a specified filter string to simulate real-time data reading.
 */
public class GNSSSimulator  extends BufferedReader {
    private String mFilename;
    private int mSleep;
    private String mFilter;

    /**
     * Constructs a GNSSSimulator with the specified filename, sleep interval, and filter string.
     *
     * @param _filename The name of the file to read GNSS data from.
     * @param _sleep The sleep interval in milliseconds to wait after reading a line containing the filter string.
     * @param _filter The filter string to search for in each line of the file.
     * @throws FileNotFoundException If the specified file does not exist.
     */
    public GNSSSimulator(String _filename, int _sleep, String _filter) throws FileNotFoundException {
        super(new FileReader(String.valueOf(Paths.get("GPS-Logs/" + _filename))));
        mFilename = _filename;
        mSleep = _sleep;
        mFilter = _filter;
    }

    /**
     * Reads a line from the file. If the line contains the filter string, the method sleeps for the specified
     * interval before returning the line.
     *
     * @return The next line from the file, or null if the end of the file has been reached.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public String readLine() throws IOException {
        String line = super.readLine();
        if (line != null && line.contains(mFilter)) {
            try {
                Thread.sleep(mSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return line;
    }

    /**
     * Gets the filter string used to determine which lines cause a delay.
     *
     * @return The filter string.
     */
    public String getFilter() {
        return mFilter;
    }
}
