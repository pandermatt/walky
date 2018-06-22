package main.java.openstreetmapparser;

/**
 * A class for storing Points with double-values
 *
 * @author Pascal Andermatt, Jan Huber
 */
public final class PointF {

    //coordinates

    public double x;
    public double y;

    /**
     * Creates a new point on a given coordinate
     */
    public PointF(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
