package main.java.mapElements;

import main.java.math.RandomGenerator;
import main.java.pedestrians.AbstractPedestrian;
import main.java.pedestrians.Drawable;
import main.java.polygonAlgorithms.ConvexHullGenerator;
import main.java.polygonAlgorithms.PolygonEnlarger;
import main.java.polygonAlgorithms.PolygonHelper;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A wall is an obstacle for a pedestrian. A pedestrian has to find a path
 * without goint through any wall
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Wall extends Drawable implements Serializable {

    //every wall can have a title  - used to import maps
    private String title;
    //convex hull is cached
    private Polygon largeConvexHullCache;
    private Polygon normalConvexHullCache;

    /**
     * Creates a new Wall
     */
    private Wall() {
        //set a random color for the wall
        super(RandomGenerator.randomBrightColor());
        //initialize all variables
        largeConvexHullCache = null;
        normalConvexHullCache = null;
        title = "";

    }

    /**
     * Creates a new Wall with a given Polygon
     *
     * @param polygon the polygon that represents the wall
     */
    public Wall(Polygon polygon) {
        this();
        currentEdges = new ArrayList<>();
        currentEdges.add(polygon);

    }

    /**
     * Creates a polygon from a given PathIterator (Code from the internet)
     */
    private static void toPolygon(PathIterator p_path, Polygon mask_tmp) {
        double[] point = new double[2];
        if (p_path.currentSegment(point) != PathIterator.SEG_CLOSE) {
            //add new Point to the Polygon
            mask_tmp.addPoint((int) point[0], (int) point[1]);
        }
    }

    /**
     * Returns if a wall is touched by a given point
     */
    public boolean doesTouch(Point check) {
        for (Polygon currentPolygon: currentEdges) {
            //is point inside wall?
            if (currentPolygon.contains(check)) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns if a pedesttrian touches the wall
     */
    public boolean intersectsLines(AbstractPedestrian pedestrian) {

        //split wall into lines
        for (Line2D.Double line: getLines()) {
            //check if distance from pedestrian to line is larger than its expansion
            if (line.ptSegDist(pedestrian.currentLocation) < pedestrian.getRadius()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts this wall into a list of line-segments
     */
    public ArrayList<Line2D.Double> getLines() {

        ArrayList<Line2D.Double> returnList = new ArrayList<>();

        //get lines from single polygon
        for (Polygon current: currentEdges) {
            returnList.addAll(PolygonHelper.getLines(current));
        }
        return returnList;
    }

    /**
     * Expands every polygon of this wall and returns it as a list of
     * line-segments
     */
    public ArrayList<Line2D.Double> getExpandedLines(AbstractPedestrian a) {

        ArrayList<Line2D.Double> returnList = new ArrayList<>();

        //for every polygon
        for (Polygon current: currentEdges) {

            //expand the polygon and get the lines
            returnList.addAll(PolygonHelper.getLines(
                    PolygonEnlarger.expandPolygon(current, a.getRadius())));

        }
        return returnList;
    }

    /**
     * Checks if a wall intersects this wall
     */
    public boolean intersectsWall(Wall toCheck) {

        //get all polygons from this wall
        for (Polygon thisPolygon: getCurrentEdges()) {
            //get all polygons from the other polygons
            for (Polygon otherPolygon: toCheck.getCurrentEdges()) {
                //check for every point....
                ArrayList<Point> p1 = PolygonHelper.getPointsFromPolygon(thisPolygon);
                for (Point p: p1) {
                    //does the other polygon contain a point of this polygon?
                    if (otherPolygon.contains(p)) {
                        return true;
                    }
                }

                //does this polygon contain a point of the other polygon?
                ArrayList<Point> p2 = PolygonHelper.getPointsFromPolygon(otherPolygon);
                for (Point p: p2) {
                    if (thisPolygon.contains(p)) {
                        return true;
                    }
                }

                //check if the lines of the polygon intersect
                //Get the lines for both polygons
                ArrayList<Line2D.Double> lines = getLines();
                ArrayList<Line2D.Double> lines2 = toCheck.getLines();

                //is there any line-intersection?
                for (Line2D.Double line: lines) {
                    for (Line2D.Double line2: lines2) {
                        if (line.intersectsLine(line2)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //code aus dem Internet...

    /**
     * Merges this wall with another wall
     *
     * @param mergeWith the wall with which this wall should be merged
     */
    public void merge(Wall mergeWith) {
        //add the other polygon to this wall
        currentEdges.addAll(mergeWith.getCurrentEdges());
        largeConvexHullCache = null;
    }

    /**
     * Merge two polygons to a single polygon. Can sometimes result in wrong
     * merging.
     */
    private Polygon merge(Polygon p1, Polygon p2) {
        //check if there are points
        if (p1.npoints == 0) {
            return p2;
        } else if (p2.npoints == 0) {
            return p1;
        }

        //convert the polygons to an arey
        Area a = new Area(p1);
        Area b = new Area(p2);
        a.add(b);

        //loop through the path
        Polygon mask_tmp = new Polygon();
        PathIterator path = a.getPathIterator(null);
        while (!path.isDone()) {
            //convert every point
            toPolygon(path, mask_tmp);
            path.next();
        }

        //return merged polygon
        return mask_tmp;
    }

    /**
     * Returns the enlarged convex hull for this wall.
     *
     * @param radius how much should the hull be enlarged
     */
    public Polygon getLargerHull(int radius) {
        return PolygonEnlarger.expandPolygon(getSingleMergedPolygon(), radius);
    }

    /**
     * Converts the wall into a single convex hull for a given pedestrian
     *
     * @param pedestrian the convex hull is expanded by the expansion of the
     *                   pedestrian
     */
    public Polygon getSingleConvexHull(AbstractPedestrian pedestrian) {

        //cacher could be added
        int radius;
        Polygon convexPolygon = null;

        //check expansion
        if (pedestrian == null) {
            radius = 0;
        } else {
            radius = pedestrian.getRadius();
        }

        //get single points
        if (convexPolygon == null) {
            //convert convex hull
            ArrayList<Point> allPoints = getInsideConvexHullPoints(pedestrian.getRadius());
            convexPolygon = new Polygon();
            for (Point point: allPoints) {
                convexPolygon.addPoint(point.x, point.y);
            }
            //return convex hull
            return convexPolygon;
        }

        //return cache-value
        //get convex hull
        largeConvexHullCache = PolygonEnlarger.expandPolygon(getSingleConvexHullNotLarger(), radius);
        return largeConvexHullCache;

    }

    /**
     * Returns the convex hull of this polygon that was not enlarged
     */
    private Polygon getSingleConvexHullNotLarger() {
        //read cache
        if (normalConvexHullCache == null) {
            //store convex hull in cache
            normalConvexHullCache = ConvexHullGenerator.createQuickHull(getSingleMergedPolygon());
        }

        //returns the convex hull
        return normalConvexHullCache;

    }

    /**
     * Returns the original convex hull of this wall
     *
     * @param forPedestrian the convex hull is expanded by the expansion of the
     *                      forPedestrian
     */
    public Polygon getOriginalConvexHull(AbstractPedestrian forPedestrian) {

        int expansion;
        //get expansion
        if (forPedestrian == null) {
            expansion = 0;
        } else {
            expansion = forPedestrian.getRadius();
        }

        //create convex hull;
        largeConvexHullCache = PolygonEnlarger.expandPolygon(getSingleConvexHullNotLarger(), expansion);

        //return hull
        return largeConvexHullCache;

    }

    /**
     * Converts this wall into a single merged polygon
     *
     * @return
     */
    private Polygon getSingleMergedPolygon() {
        Polygon fullPolygon = new Polygon();
        for (Polygon current: getCurrentEdges()) { //for each polygon
            fullPolygon = merge(fullPolygon, current); //merge with the other polygon
        }
        return fullPolygon;
    }

    /**
     * Returns every edege of this wall
     *
     * @param radius how much should the polygons be enlarged?
     */
    private ArrayList<Point> getInsideConvexHullPoints(int radius) {
        ArrayList<Point> possibleWalkingPoints = new ArrayList<>();
        for (Polygon p: currentEdges) { //for every polygon...
            //add the enlarged
            possibleWalkingPoints.addAll(PolygonHelper.getPointsFromPolygon(PolygonEnlarger.expandPolygon(p, radius)));
        }
        return possibleWalkingPoints;
    }

    /**
     * Returns if a given Point equals an edge of this wall
     */
    public boolean isEdge(Point point) {
        for (Polygon p: getCurrentEdges()) { //for every polygon
            for (Point p2: PolygonHelper.getPointsFromPolygon(p)) { //get points from polygon
                if (p2.equals(point)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*Setter and Getter*/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * returns all Points of this wall
     */
    public ArrayList<Point> getAllPoints() {

        ArrayList<Point> returnList = new ArrayList<>();
        for (Polygon p: getCurrentEdges()) { //for every polygon...
            returnList.addAll(PolygonHelper.getPointsFromPolygon(p)); //get Points
        }
        return returnList;
    }

}
