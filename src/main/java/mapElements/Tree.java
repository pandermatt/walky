package main.java.mapElements;

import main.java.pedestrians.Drawable;
import main.java.pedestriansimulator.ApplicationSingletone;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A tree is an image drawn on the screen. Trees can by imported from
 * OpenStreetMaps-maps
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Tree extends Drawable implements Serializable {

    private final int radius;

    /**
     * Creates a new tree on a given location
     */
    public Tree(double x, double y) {
        super(Color.white);
        this.x = x;
        this.y = y;
        radius = 3;
    }

    /**
     * Returns an image of a tree
     */
    public Image getImage() {
        //load image
        return ApplicationSingletone.getThemeManager().getGraphic("tree", "png");
    }

    /**
     * Updates the coordinates of a tree for a given location-size
     *
     * @param screenDimension the original dimension of a screen
     * @param view            the current screen
     */
    public void updateCoordinates(Dimension screenDimension, Rectangle2D.Double view) {

        //the converted points will be saved here
        ArrayList<Point> convertedPoints = new ArrayList<>();

        //calculate new x
        double x = getX();
        double width = view.width;
        double coppedX = x - view.x;
        double percentX = coppedX / width;
        double newX = percentX * screenDimension.width;

        //calculate new y
        double y = getY();
        double height = view.height;
        double coppedY = y - view.y;
        double percentY = coppedY / height;
        double newY = percentY * screenDimension.height;

        convertedPoints.add(new Point(Math.round((float) newX), Math.round((float) newY)));

        //set new values
        setX(newX);
        setY(newY);

    }

    /*Setter and Getter*/
    public int getX() {
        //round value
        return Math.round((float) x);
    }

    private void setX(double x) {
        this.x = x;
    }

    public int getY() {
        //round value
        return Math.round((float) y);
    }

    private void setY(double y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

}
