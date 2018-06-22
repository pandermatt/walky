package main.java.openstreetmapparser;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import main.java.mapElements.Wall;

/**
 * A building is imported from a map and can be converted to a "wall" later
 * @author Pascal Andermatt, Jan Huber
 */
public class Building {
    ArrayList<Location> edges; //the edges of the building
    String title; //a short description of the building

    /**
     * Creates a new Building with given edges
     */
    public Building(ArrayList<Location> edges) {
        this.edges = edges;
        title = "";
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
            double coppedX = x-currentView.x;
            double percentX = coppedX/width;
            double newX = percentX*screenDimension.width;
            
            //calculate new y location
            double y = location.getProjectionPoint().y;
            double height = currentView.height;
            double coppedY = y-currentView.y;
            double percentY = coppedY/height;
            double newY = percentY*screenDimension.height;
            
            //round location and add to converted points
            convertedPoints.add(new Point(Math.round((float)newX), Math.round((float)newY)));
        }
        //build wall from converted points
        Wall returnWall = new Wall(getPolygonFromPoints(convertedPoints));
        //update title 
        returnWall.setTitle(title);
        return returnWall;
        
    }
    
    
    private static Polygon getPolygonFromPoints(ArrayList<Point> points) { 
        Polygon returnPolygon = new Polygon();
        for (int i = 0; i < points.size(); i++) {
            returnPolygon.addPoint(points.get(i).x, points.get(i).y);
        }
        return returnPolygon;
    }
    
    /*Setter and Getter*/

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
     public ArrayList<Location> getEdges() {
        return edges;
    }
    
    
    
}
