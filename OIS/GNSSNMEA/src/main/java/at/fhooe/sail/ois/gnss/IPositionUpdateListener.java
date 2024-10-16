package at.fhooe.sail.ois.gnss;

/**
 * IPositionUpdateListener is an interface that defines a method to handle position updates.
 * Implementing classes should provide the logic for handling updates when new GNSS data is available.
 */
public interface IPositionUpdateListener {
    /**
     * Called when there is an update to the GNSS position data.
     *
     * @param _info The NMEAInfo object containing the updated GNSS data.
     */
    void update(NMEAInfo _info);
}
