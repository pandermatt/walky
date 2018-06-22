package main.java.polygonAlgorithms;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

//Algorithm 'inspired' from here: http://stackoverflow.com/questions/3749678/expand-fill-of-convex-polygon
/**
 * This class can expand a given polygon by a given radius
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class PolygonEnlarger {

    /**
     * Decides if the Points from a given Polygon are ordered clockwiese or
     * anti-clockwise
     */
    public static boolean isClockwise(Polygon poly) {

        int sum = 0;

        //for each point
        for (int i = 0; i < poly.npoints; i++) {

            //get next and current point
            Point currentPoint = new Point(poly.xpoints[i], poly.ypoints[i]);
            Point nextPoint = new Point(poly.xpoints[i + 1 >= poly.npoints ? 0 : i + 1], poly.ypoints[i + 1 >= poly.npoints ? 0 : i + 1]);
            //update sum
            sum += ((nextPoint.x - currentPoint.x) * (nextPoint.y + currentPoint.y));
        }

        //is the polygon clockwise or anti-clockwiese
        return sum <= 0;

    }

    /**
     * Shifts a given line.
     */
    public static Line2D.Double paralellShift(Line2D.Double oldLine, int amount, boolean isClockwise) {

        //Calculate difference in x- and y-direction
        double xDifference = oldLine.x2 - oldLine.x1;
        double yDifference = oldLine.y2 - oldLine.y1;

        double yChange = 0;
        double xChange = 0;
        //update difference
        if (isClockwise) {
            yChange = -xDifference;
            xChange = yDifference;
        } else {
            yChange = xDifference;
            xChange = -yDifference;
        }

        //use pythagoras to calculate length
        double length = Math.sqrt(xChange * xChange + yChange * yChange);
        double divisor = length / amount;

        //update x- and y-change
        xChange /= divisor;
        yChange /= divisor;

        //return the shifted line
        return new Line2D.Double(oldLine.x1 + xChange, oldLine.y1 + yChange, oldLine.x2 + xChange, oldLine.y2 + yChange);

    }

    //Quelle: http://www.ahristov.com/tutorial/geometry-games/intersection-lines.html
    /**
     * Computes the intersection between two lines. The calculated point is
     * approximate, since integers are used. If you need a more precise result,
     * use doubles everywhere. (c) 2007 Alexander Hristov. Use Freely (LGPL
     * license). http://www.ahristov.com
     *
     * @param x1 Point 1 of Line 1
     * @param y1 Point 1 of Line 1
     * @param x2 Point 2 of Line 1
     * @param y2 Point 2 of Line 1
     * @param x3 Point 1 of Line 2
     * @param y3 Point 1 of Line 2
     * @param x4 Point 2 of Line 2
     * @param y4 Point 2 of Line 2
     * @return Point where the segments intersect, or null if they don't
     */
    private static Point intersection(
            double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4
    ) {
        //use this formula to calculate intersection-Point
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point(Math.round((float) xi), (int) Math.round(yi));
    }
    

    /**
     * Expands a given polygon.
     * @param polygon the polygon that should be enlarged
     * @param expansion how much should the polygon be enlarged?
     * @return the enlarged polygons
     */
    public static Polygon expandPolygon(Polygon polygon, int expansion) {
        Polygon returnPoly = new Polygon();
        boolean isClockwise = isClockwise(polygon);
        //convert polygon to lines
        ArrayList<Line2D.Double> lines = PolygonHelper.getLines(polygon);

        //for each line
        for (int i = 0; i < lines.size(); i++) {
            Line2D.Double currentLineUnshift = lines.get(i);
            Line2D.Double nextLineUnshift;

            //get current lines
            if (i == lines.size() - 1) {
                nextLineUnshift = lines.get(0);
            } else {
                nextLineUnshift = lines.get(i + 1);
            }

            //shift current lines
            Line2D.Double currentLine = paralellShift(currentLineUnshift, expansion, isClockwise); //gibt manchmal nan zurück??
            Line2D.Double nextLine = paralellShift(nextLineUnshift, expansion, isClockwise);
            
            //get intersection Point
            Point intersection = intersection(currentLine.x1, currentLine.y1, currentLine.x2, currentLine.y2, nextLine.x1, nextLine.y1, nextLine.x2, nextLine.y2);

            //add intersection-point to polygon
            if (intersection != null && (!intersection.equals(new Point(0, 0)))) {
                returnPoly.addPoint(intersection.x, intersection.y);
            }
        }
        return returnPoly;
    }
}
