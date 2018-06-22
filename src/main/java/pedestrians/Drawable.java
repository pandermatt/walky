package main.java.pedestrians;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Drawable is an abstract class that contains information abount classes that
 * can be drawn onto the screen
 *
 * @author Pascal Andermatt, Jan Huber
 */
public abstract class Drawable implements Serializable {

    protected ArrayList<Polygon> currentEdges;
    boolean selected;
    //location
    protected double x;
    protected double y;
    //values
    private Color color;

    /**
     * Creates a new drawable abstract Object
     *
     * @param color
     */
    protected Drawable(Color color) {
        this.color = color;
        this.currentEdges = null;
        this.selected = false;
    }

    /**
     * switches the value of 'selected'
     */
    public void toggleSelected() {
        selected = !selected;
    }

    /*Setter and Getter*/

    public ArrayList<Polygon> getCurrentEdges() {
        return currentEdges;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Color getColor() {
        if (isSelected()) {
            return Color.CYAN; //if selected, a drawable object always has the same color
        }
        return color;
    }

    void setColor(Color color) {
        this.color = color;
    }

    protected Color getOriginalColor() {
        return color;
    }
}
