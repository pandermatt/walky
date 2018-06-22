package main.java.openstreetmapparser;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class calculates a minimum view-size that contains every building in a
 * list
 *
 * @author Pascal Andermatt, Jan Huber
 */
class BuildingTransformer {

    public static Rectangle2D.Double calculateViewSize(ArrayList<Building> buildings) {
        //variables for the extremal-points
        Double maxX = null, minX = null, maxY = null, minY = null;
        for (Building building : buildings) { //for each building
            for (Location edge : building.getEdges()) { //Get all edges
                Point2D.Double projection = edge.getProjectionPoint(); //apply point-protection
                double x = projection.getX();
                double y = projection.getY();

                //check if the current Point is a new extremal-point
                if (maxX == null || maxX < x) {
                    maxX = x;
                }

                if (minX == null || minX > x) {
                    minX = x;
                }

                if (maxY == null || maxY < y) {
                    maxY = y;
                }

                if (minY == null || minY > y) {
                    minY = y;
                }
            }
        }

        //return the minimal view-size
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);

    }

}
