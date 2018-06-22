package main.java.polygonAlgorithms;

import com.vividsolutions.jts.geom.*;
import java.util.ArrayList;

/**
 * This class checks if a polygon is self-intersecting
 * @author Pascal Andermatt, Jan Huber
 */
public class PolygonIntersectionChecker {

    public static boolean isSelfIntersecting(java.awt.Polygon p) {
        //Convert the polygon  to be compatible to the "pipeep-library"
        Geometry geom = convertPolygonForLibrary(p);
        if (geom.isValid()) {
            geom.normalize();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert a polygon to be compatible to the "pipeep-library"
     */
    private static Polygon convertPolygonForLibrary(java.awt.Polygon p) {

        //get all points to a polygon
        ArrayList<java.awt.Point> points = PolygonHelper.getPointsFromPolygon(p);
        
        //convert each point to a coordinate
        Coordinate[] coordinates = new Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            coordinates[i] = new Coordinate(points.get(i).x, points.get(i).y);
        }
        coordinates[points.size()] = new Coordinate(points.get(0).x, points.get(0).y);

        //build polygon from coordinates
        GeometryFactory fact = new GeometryFactory();
        LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
        Polygon poly = new Polygon(linear, null, fact);
        return poly;
    }

}
