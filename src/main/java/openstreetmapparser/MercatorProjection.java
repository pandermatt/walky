package main.java.openstreetmapparser;

/**
 * This class is used to convert a lat/lon point into x and y coordinates using
 * mercator-projection. A similar class is used by GoogleMaps
 *
 * @author Pascal Andermatt, Jan Huber
 * @see Wikipedia: MercatorProjection
 * @see GoogleMaps: Projection class
 */
final class MercatorProjection {

    private final PointF originalPixel;
    private final double pixelsPerDegreeLon;
    private final double pixelsPerRadionLon;

    /**
     * Creates a new MercatorProjection-Object
     */
    public MercatorProjection() {
        int SINGLE_TILE_SIZE = 256;
        this.originalPixel = new PointF(SINGLE_TILE_SIZE / 2.0, SINGLE_TILE_SIZE / 2.0);
        this.pixelsPerDegreeLon = SINGLE_TILE_SIZE / 360.0; //360 degree
        this.pixelsPerRadionLon = SINGLE_TILE_SIZE / (2 * Math.PI); //same calculation in radion
    }

    /**
     * convert a lat/lon point into x and y coordinates using
     * mercator-projection.
     */
    PointF applyMercatorProjection(double lat, double lng) {
        //initialize converted point
        PointF point = new PointF(0, 0);

        //multiply x location
        point.x = originalPixel.x + lng * pixelsPerDegreeLon;

        //apply algorithm
        double siny = getMinimalValue(Math.sin(convertDegreesToRadians(lat)));
        point.y = originalPixel.y + 0.5 * Math.log((1 + siny) / (1 - siny)) * -pixelsPerRadionLon;

        //use bit-shift operation
        int tiles = 1 << 15;

        //multiply with amount of tiles
        point.x = point.x * tiles;
        point.y = point.y * tiles;

        //Return converted point 
        return point;
    }

    /*Private Helper Methods*/
    private double getMinimalValue(double val) {
        //return smaller value
        return Math.min(val, 0.9999);
    }

    private double convertDegreesToRadians(double degrees) {
        //convert a value in degrees into the radian-syste,
        return degrees * (Math.PI / 180);
    }

}
