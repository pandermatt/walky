package main.java.openstreetmapparser;

import main.java.mapElements.Wall;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * A building is imported from a map and can be converted to a "wall" later
 *
 * @author Pascal Andermatt, Jan Huber
 */
class Building {
    private final ArrayList<Location> edges; //the edges of the building
    private String title; //a short description of the building

    /**
     * Creates a new Building with given edges
     */
    public Building(ArrayList<Location> edges) {
        this.edges = edges;
        title = "";
    }

    private static Polygon getPolygonFromPoints(ArrayList<Point> points) {
        Polygon returnPolygon = new Polygon();
        for (Point point: points) {
            returnPolygon.addPoint(point.x, point.y);
        }
        return returnPolygon;
    }

    /**
     * Converts this building into a wall for a given screenDimension
     */
    public Wall toWall(Dimension screenDimension, Rectangle2D.Double currentView) {

        ArrayList<Point> convertedPoints = new ArrayList<>();

        //loop through every edge of the building
        for (Location location: edges) {
            //calculate new x location
            double x = location.getProjectionPoint().x;
            double width = currentView.width;
            double coppedX = x - currentView.x;
            double percentX = coppedX / width;
            double newX = percentX * screenDimension.width;

            //calculate new y location
            double y = location.getProjectionPoint().y;
            double height = currentView.height;
            double coppedY = y - currentView.y;
            double percentY = coppedY / height;
            double newY = percentY * screenDimension.height;

            //round location and add to converted points
            convertedPoints.add(new Point(Math.round((float) newX), Math.round((float) newY)));
        }
        //build wall from converted points
        Wall returnWall = new Wall(getPolygonFromPoints(convertedPoints));
        //update title
        returnWall.setTitle(title);
        return returnWall;

    }

    /*Setter and Getter*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Location> getEdges() {
        return edges;
    }


}
