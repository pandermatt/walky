package main.java.polygonAlgorithms;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This class is used to calculate the convex hull around a polygon
 */
public class ConvexHullGenerator {
    
    /*
        Der folgende Code stammt teilweise aus dem Internet, wurde aber von uns angepasst
    */
    
    
    /*
     * Copyright (c) 2007 Alexander Hristov.
     * http://www.ahristov.com
     * 
     * Feel free to use this code as you wish, as long as you keep this copyright
     * notice. The only limitation on use is that this code cannot be republished
     * on other web sites. 
     *
     * As usual, this code comes with no warranties of any kind.
     *
     * 
     */
    
    
    /**
     * Creates a new convex hull around a given polygon.
     */
    public static Polygon createQuickHull(Polygon polygon) { 
        //convert polygon to pooint list
        ArrayList<Point> allPoints = PolygonHelper.getPointsFromPolygon(polygon);
        ArrayList<Point> convexHull = new ArrayList<>();
        if (allPoints.size() <= 3) {
            //three or less points do not require a convex hull
            return PolygonHelper.getPolygonFromPoints((ArrayList<Point>)allPoints.clone());
        }
        // find extremal-points
        int minimalPoint = -1, maxPoint = -1;
        int minimalX = Integer.MAX_VALUE;
        int maximalX = Integer.MIN_VALUE;
        for (int i = 0; i < allPoints.size(); i++) {
            if (allPoints.get(i).x < minimalX) {
                minimalX = allPoints.get(i).x;
                minimalPoint = i;
            }
            if (allPoints.get(i).x > maximalX) {
                maximalX = allPoints.get(i).x;
                maxPoint = i;
            }
        }
        
        //add extremal-Points to convex hull 
        Point pointMinimum = allPoints.get(minimalPoint);
        Point pointMaximum = allPoints.get(maxPoint);
        convexHull.add(pointMinimum);
        convexHull.add(pointMaximum);
        allPoints.remove(pointMinimum);
        allPoints.remove(pointMaximum);

        //convert point-list into two different sets
        ArrayList<Point> leftSet = new ArrayList<Point>();
        ArrayList<Point> rightSet = new ArrayList<Point>();

        //decide for each Point to which set it belongs
        for (int i = 0; i < allPoints.size(); i++) {
            Point p = allPoints.get(i);
            if (calculateLocation(pointMinimum, pointMaximum, p) == -1) {
                leftSet.add(p);
            } else {
                rightSet.add(p);
            }
        }
        
        //each set is precessed seperately
        recursiveProcessSet(pointMinimum, pointMaximum, rightSet, convexHull);
        recursiveProcessSet(pointMaximum, pointMinimum, leftSet, convexHull);

        //return convex hull
        return PolygonHelper.getPolygonFromPoints(convexHull);
    }

    /*
     * Calculates the square distance of point p3 to the segment p1-p2
     */
    private static int distance(Point p1, Point p2, Point p3) {
        int xDifference = p2.x - p1.x;
        int yDifference = p2.y - p1.y;
        int result = xDifference * (p1.y - p3.y) - yDifference * (p1.x - p3.x);
        return Math.abs(result);
    }

    /**
     * Recusively calculates the convex hull around a given set of points.
     */
    private static void recursiveProcessSet(Point firstPoint, Point secondPoint, ArrayList<Point> pointSet, ArrayList<Point> temporaryHull) {
        int nextPoint = temporaryHull.indexOf(secondPoint);
        if (pointSet.isEmpty()) {
            return;
        }
        //if the set has only 1 Point, is is always part of the convex hull
        if (pointSet.size() == 1) {
            Point p = pointSet.get(0);
            pointSet.remove(p);
            temporaryHull.add(nextPoint, p);
            return;
        }
        
        //calculate the distance to the line for each Pont
        int maximalDistance = Integer.MIN_VALUE;
        int maximalDistancePoint = -1;
        for (int i = 0; i < pointSet.size(); i++) {
            Point p = pointSet.get(i);
            int distance = distance(firstPoint, secondPoint, p);
            if (distance > maximalDistance) {
                maximalDistance = distance;
                maximalDistancePoint = i;
            }
        }
        Point P = pointSet.get(maximalDistancePoint);
        pointSet.remove(maximalDistancePoint);
        
        //the point with the maximal disctance is part of the convex hull
        temporaryHull.add(nextPoint, P);

        //Which is the set on the left side?
        
        //add Points to left set
        ArrayList<Point> leftSet = new ArrayList<Point>();
        for (int i = 0; i < pointSet.size(); i++) {
            Point currentPoint = pointSet.get(i);
            if (calculateLocation(firstPoint, P, currentPoint) == 1) {
                leftSet.add(currentPoint);
            }
        }

        //Add Point to the right set
        ArrayList<Point> rightSet = new ArrayList<Point>();
        for (int i = 0; i < pointSet.size(); i++) {
            Point M = pointSet.get(i);
            if (calculateLocation(P, secondPoint, M) == 1) {
                rightSet.add(M);
            }
        }
        
        //recursiv call for this method
        recursiveProcessSet(firstPoint, P, leftSet, temporaryHull);
        recursiveProcessSet(P, secondPoint, rightSet, temporaryHull);

    }

    //decide if a point belongs to the left or the right side of a line
    private static int calculateLocation(Point p1, Point p2, Point p3) {
        int location = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
        return (location > 0) ? 1 : -1;
    }
    

}
