package main.java.math;

import java.awt.Point;

/**
 * This class is able to find the nearest point inside a line-segment for a
 * given point.
 */
public class NearestPointInLine {

    /**
     * Find the nearest point inside a line-segment for a given point (Algorithm
     * from the internet)
     */
    public static Point minimalDistancePoint(Point currentPoint, Point linePoint1, Point linePoint2) {

        //get coordinates from the currentPoint
        double x = currentPoint.x;
        double y = currentPoint.y;

        //get coordinates from linePoint1
        double x1 = linePoint1.x;
        double y1 = linePoint1.y;

        //get coordinates from linePoint2
        double x2 = linePoint2.x;
        double y2 = linePoint2.y;

        //substract the values from each other
        double valueA = x - x1;
        double valueB = y - y1;
        double valueC = x2 - x1;
        double valueD = y2 - y1;

        //multiply values
        double dot = valueA * valueC + valueB * valueD;
        double len_sq = valueC * valueC + valueD * valueD;
        double param = -1;
        if (len_sq != 0) { //in case the length of the line is 0
            param = dot / len_sq;
        }
        double xx, yy; //resultvalues

        if (param < 0) { //edge is the fastest way
            xx = x1;
            yy = y1;
        } else if (param > 1) {//edge is the fastest way
            xx = x2;
            yy = y2;
        } else { //find minimal distance
            xx = x1 + param * valueC;
            yy = y1 + param * valueD;
        }

        //return Point with the minimal distance
        return new Point(Math.round((float) xx), Math.round((float) yy));
    }
}
