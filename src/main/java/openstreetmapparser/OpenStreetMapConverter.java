package main.java.openstreetmapparser;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import main.java.mapElements.Tree;
import main.java.mapElements.Wall;

/**
 * The OpenStreetMapConverter converts an XML-File into Java-Classes (Tree,
 * Wall, ...)
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class OpenStreetMapConverter {

    public static ArrayList<Tree> trees;

    /**
     * Convert the XML-File into a list of walls
     */
    public static ArrayList<Wall> getWallsFromMap(String filename, Dimension screenDimension) throws IOException, Exception {
        ArrayList<Wall> walls = new ArrayList<>();
        //Read the file
        OpenStreetMapParser reader = new OpenStreetMapParser(filename);
        //Get the buildings
        ArrayList<Building> buildings = reader.getBuildings();
        //convert building-location
        Rectangle2D.Double rectangle = BuildingTransformer.calculateViewSize(buildings);
        System.out.println("All Buildings are loaded");
        //convert every building to a wall
        for (Building building : buildings) {
            walls.add(building.toWall(screenDimension, rectangle));
        }
        //get trees from the map
        trees = reader.getTrees(screenDimension, rectangle);
        return walls;
    }

    /*Setter and Getter*/
    public static ArrayList<Tree> getTrees() {
        return trees;
    }

}
