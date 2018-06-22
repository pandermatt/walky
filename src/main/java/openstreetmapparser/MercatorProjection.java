package main.java.openstreetmapparser;

/**
 * This class is used to convert a lat/lon point into x and y coordinates using
 * mercator-projection. A similar class is used by GoogleMaps
 *
 * @see Wikipedia: MercatorProjection
 * @see GoogleMaps: Projection class
 * @author Pascal Andermatt, Jan Huber
 */
public final class MercatorProjection {

    private final int SINGLE_TILE_SIZE = 256;
    private PointF originalPixel;
    private double pixelsPerDegreeLon;
    private double pixelsPerRadionLon;

    /**
     * Creates a new MercatorProjection-Object
     */
    public MercatorProjection() {
        this.originalPixel = new PointF(SINGLE_TILE_SIZE / 2.0, SINGLE_TILE_SIZE / 2.0);
        this.pixelsPerDegreeLon = SINGLE_TILE_SIZE / 360.0; //360 degree
        this.pixelsPerRadionLon = SINGLE_TILE_SIZE / (2 * Math.PI); //same calculation in radion
    }

    /**
     * convert a lat/lon point into x and y coordinates using
     * mercator-projection.
     */
    PointF applyMercatorProjection(double lat, double lng, int zoom) {
        //initialize converted point
        PointF point = new PointF(0, 0);

        //multiply x location
        point.x = originalPixel.x + lng * pixelsPerDegreeLon;

        //apply algorithm
        double siny = getMinimalValue(Math.sin(convertDegreesToRadians(lat)), 0.9999);
        point.y = originalPixel.y + 0.5 * Math.log((1 + siny) / (1 - siny)) * -pixelsPerRadionLon;

        //use bit-shift operation
        int tiles = 1 << zoom;

        //multiply with amount of tiles
        point.x = point.x * tiles;
        point.y = point.y * tiles;

        //Return converted point 
        return point;
    }

    /*Private Helper Methods*/
    private double getMinimalValue(double val, double valMax) {
        //return smaller value
        return Math.min(val, valMax);
    }

    private double convertDegreesToRadians(double degrees) {
        //convert a value in degrees into the radian-syste,
        return degrees * (Math.PI / 180);
    }

}
