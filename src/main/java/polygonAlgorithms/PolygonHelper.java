package main.java.polygonAlgorithms;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This class provides some static methods to work with polygons
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class PolygonHelper {

    /**
     * converts a given polygon to a list of Points
     */
    public static ArrayList<Point> getPointsFromPolygon(Polygon polygon) {

        ArrayList<Point> returnList = new ArrayList<>();
        //for each point
        for (int i = 0; i < polygon.npoints; i++) {
            //add point
            returnList.add(new Point(polygon.xpoints[i], polygon.ypoints[i]));
        }
        return returnList;
    }

    /**
     * converts a list of points to a polygon
     */
    static Polygon getPolygonFromPoints(ArrayList<Point> points) {
        Polygon returnPolygon = new Polygon();
        for (int i = 0; i < points.size(); i++) {
            returnPolygon.addPoint(points.get(i).x, points.get(i).y);
        }
        return returnPolygon;
    }

    /**
     * Converts a polygon into a list of lines
     */
    public static ArrayList<Line2D.Double> getLines(Polygon current) {
        ArrayList<Line2D.Double> returnList = new ArrayList<>();
        //convert the polygon to a list of poitns
        ArrayList<Point> points = PolygonHelper.getPointsFromPolygon(current);
        for (int i = 0; i < points.size(); i++) {
            Point2D.Double currentPoint;
            //get current Point
            currentPoint = new Point2D.Double(points.get(i).x, points.get(i).y);
            if (i == points.size() - 1) {
                //connect last Point to Point
                Point2D.Double firstPoint;
                firstPoint = new Point2D.Double(points.get(0).x, points.get(0).y);
                //add line to list
                returnList.add(new Line2D.Double(currentPoint, firstPoint));
                continue;
            }
            //connect Point to first Point
            returnList.add(new Line2D.Double(currentPoint, new Point2D.Double(points.get(i + 1).x, points.get(i + 1).y)));
        }
        return returnList;
    }
}
