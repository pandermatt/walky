package main.java.gui;

import main.java.pedestriansimulator.ApplicationSingletone;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * The zoom and pan canvas is used to draw the animations on it
 */
public class PedestrianComponent extends JPanel implements Observer, Serializable {

    public final ZoomMouseListener zoomAndPanListener;
    private boolean init = true; //init is true if no frame was drawn yet
    private final PedestrianPanel panel;
    //set the points to their default location
    private Point[] points = {
            new Point(-100, -100),
            new Point(-100, 100),
            new Point(100, -100),
            new Point(100, 100)
    };

    /**
     * Creates a ZoomAndPanCanvas
     */
    PedestrianComponent() {
        //set all values
        panel = new PedestrianPanel();
        this.zoomAndPanListener = new ZoomMouseListener(this);
        this.addMouseListener(zoomAndPanListener);
        this.addMouseMotionListener(zoomAndPanListener);
        this.addMouseWheelListener(zoomAndPanListener);
        ApplicationSingletone.getCurrentMap().addObserver(this);

    }

    /**
     * Creates a ZoomAndPanCanvas with given zoom-settings
     */
    public PedestrianComponent(int minZoomLevel, int maxZoomLevel, double zoomMultiplicationFactor) {
        panel = new PedestrianPanel();
//update given zoom-settings
        this.zoomAndPanListener = new ZoomMouseListener(this, minZoomLevel, maxZoomLevel, zoomMultiplicationFactor);
        this.addMouseListener(zoomAndPanListener);
        this.addMouseMotionListener(zoomAndPanListener);
        this.addMouseWheelListener(zoomAndPanListener);
        ApplicationSingletone.getCurrentMap().addObserver(this);

    }

    /**
     * Returns the default size of this component
     */
    public Dimension getPreferredSize() {
        return new Dimension(600, 500);
    }

    /**
     * Is called when the component should draw itself onto a Graphics-Object
     */
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        if (init) {
            zoomAndPanListener.setTransformation(g.getTransform());
        } else {
            // Restore the viewport after it was updated by the ZoomAndPanListener
            g.setTransform(zoomAndPanListener.getTransformation());
        }

        try {
            //The Pedestrian Panel contains all the drawing-methods
            panel.draw(g);
            if (init) {
                resetZoom();
                init = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //a circle apears on the screen if somthing went wrong while drawing.
            //used for debugging
            g.drawOval(0, 0, 50, 50);
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        //update focus
        setFocusable(false);
        setFocusable(true);
        panel.recordIfNecessary();

        //repaint
        repaint();
        ApplicationSingletone.getMainWindow().updateCursor();
    }

    /**
     * Resets the zoom-factor to '1'
     */
    void resetZoom() {
        zoomAndPanListener.setStartZoom(1);
        zoomAndPanListener.setTransformation(new AffineTransform());

    }

    /**
     * Is called when a new map was imported. In this case this object should be
     * added as an observer of the map
     */
    void mapHasChanged() {
        ApplicationSingletone.getCurrentMap().addObserver(this);
        ApplicationSingletone.getCurrentMap().zoomListener = zoomAndPanListener;
        panel.mapHasChanged();
    }

    /*Setter and Getter*/
    public PedestrianPanel getPanel() {
        return panel;
    }

}
