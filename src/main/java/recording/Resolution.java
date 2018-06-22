package main.java.recording;

import java.io.Serializable;

/**
 * A recording-Object contains a dimension for x and y and a title which is
 * displayed on the recording-textfields
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Resolution implements Serializable {

    private final String title;
    //dimension
    private final int x;
    private final int y;

    /**
     * Creates a new resolution.
     */
    public Resolution(String title, int x, int y) {
        this.title = title;
        this.x = x;
        this.y = y;
    }

    /**
     * Converts a resolution to a String
     */
    @Override
    public String toString() {
        return "[" + title + "] " + x + " x " + y;
    }

    /*Setter and Getter*/

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
