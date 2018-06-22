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

    //values
    private Color color;
    public ArrayList<Polygon> currentEdges;
    public Polygon originEdges;
    public boolean selected;

    //location
    protected double x;
    protected double y;

    /**
     * Creates a new drawable abstract Object
     *
     * @param color
     */
    public Drawable(Color color) {
        this.color = color;
        this.currentEdges = null;
        this.originEdges = null;
        this.selected = false;
    }

    /**
     * switches the value of 'selected'
     */
    public void toggleSelected() {
        selected = !selected;
    }

    /*Setter and Getter*/
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<Polygon> getCurrentEdges() {
        return currentEdges;
    }

    public boolean isSelected() {
        return selected;
    }

    public Color getColor() {
        if (isSelected()) {
            return Color.CYAN; //if selected, a drawable object always has the same color
        }
        return color;
    }
    
    public Color getOriginalColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
