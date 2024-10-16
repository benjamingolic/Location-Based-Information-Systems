package at.fhooe.sail.ois.map.client;

import java.awt.image.BufferedImage;

/**
 * An interface for observing and handling updates involving BufferedImage objects.
 * Classes that implement this interface can be used to receive notifications when
 * a BufferedImage has been updated or when the scale has been updated.
 */
public interface IDataObserver {

    /**
     * This method is called when a BufferedImage update occurs.
     *
     * @param _img The updated BufferedImage.
     */
    public void update(BufferedImage _img);

    /**
     * This method is called when a scale update occurs.
     *
     * @param _scale The updated scale value.
     */
    public void updateScale(int _scale);
}
