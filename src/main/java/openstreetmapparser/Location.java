package main.java.openstreetmapparser;

import java.awt.geom.Point2D;

/**
 * A location is represented my lat and lon
 *
 * @author Pascal Andermatt, Jan Huber
 */
class Location {

    private Double lat;
    private Double lon;

    /**
     * Creates a new location with given values
     */
    public Location(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /*Setter and Getter*/
    /**
     * @return the lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * @return the lon
     */
    public Double getLon() {
        return lon;
    }

    /**
     * @param lon the lon to set
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /*Converts the lat/lon point to x and y by using MercatorProjection*/
    public Point2D.Double getProjectionPoint() {
        MercatorProjection gmap2 = new MercatorProjection();
        PointF point1 = gmap2.applyMercatorProjection(lat, lon, 15);
        return new Point2D.Double(point1.x, point1.y);
    }
}
