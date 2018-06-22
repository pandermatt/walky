package main.java.gui;

import main.java.pedestriansimulator.ApplicationSingletone;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * This class is an extended version of the following document:
 * https://github.com/sl/BuffonsNeedle/blob/master/src/com/zaptapgo/buffon/gui/ZoomAndPanListener.java
 * <p>
 * It is used to provide the PedestrianPanel with a zoom and shift funcionality
 *
 * @author Sam Lazarus, Jan Huber, Pascal Andermatt
 */
public class ZoomMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener, Serializable {

    private static final int LEVEL_MINIMUM = -50; //the maximal value to zoom in
    private static final int LEVEL_MAXIMUM = 20; //to maximal value to zoom out
    private static final double FACTOR = 1.1; //how fast should the user be able to zoom

    //the component for which the zoomins is applied
    private final Component component;

    private int startZoom = 0; //start with no zoom
    private int minimalZoom = LEVEL_MINIMUM;
    private int maximalZoom = LEVEL_MAXIMUM;
    private double factorZoom = FACTOR;

    //varialbes for drag and drop
    private Point startDragPoint;
    private Point endDragPoint;

    //the transformation for the component
    private AffineTransform transformation = new AffineTransform();

    /**
     * Creates a new ZoomMouseListener
     *
     * @param component the component for which the zoom should be applied
     */
    public ZoomMouseListener(Component component) {
        this.component = component;
    }

    /**
     * Creates a new ZoomMouseListener with custom variables
     */
    public ZoomMouseListener(Component targetComponent, int minZoomLevel, int maxZoomLevel, double zoomMultiplicationFactor) {
        this.component = targetComponent;
        this.minimalZoom = minZoomLevel;
        this.maximalZoom = maxZoomLevel;
        this.factorZoom = zoomMultiplicationFactor;
        ApplicationSingletone.getCurrentMap().zoomListener = this;
    }

    /**
     * Converts a given mousePosition into the scaled and transformed coordinate
     */
    public Point convertMousePosition(Point mousePoint) {
        if (mousePoint == null) {
            return null;
        }

        //translate x and y 
        double x = mousePoint.getX();
        double y = mousePoint.getY();
        x -= transformation.getTranslateX();
        y -= transformation.getTranslateY();

        //scale x and y 
        x *= (1 / transformation.getScaleX());
        y *= (1 / transformation.getScaleY());

        //return new rounded point
        return new Point(Math.round((float) x), (int) Math.round(y));
    }


    /**
     * Shifs the component view if the mouse was dragging
     */
    private void shiftView(MouseEvent e) {
        try {
            //get start- and endpoint
            endDragPoint = e.getPoint();
            Point2D.Float start = getTransformedPoint(startDragPoint);
            Point2D.Float end = getTransformedPoint(endDragPoint);
            //calculate distance
            double xDistance = end.getX() - start.getX();
            double yDistance = end.getY() - start.getY();
            //create translation
            transformation.translate(xDistance, yDistance);
            //apply translation
            startDragPoint = endDragPoint;
            endDragPoint = null;
            component.repaint();
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        //update view
        ApplicationSingletone.getMainWindow().updateCursor();
    }

    /**
     * Zooms the component in or out, depending on the MouseWheelEvent
     */
    private void zoomView(MouseWheelEvent e) {
        try {
            //load the wheel rotaino
            int rotation = e.getWheelRotation();
            Point p = e.getPoint();
            if (rotation > 0) {
                //is the zoom already maximalZoom?
                if (startZoom < maximalZoom) {
                    startZoom++;
                    //apply transformation
                    Point2D transformationPoint1 = getTransformedPoint(p);
                    transformation.scale(1 / factorZoom, 1 / factorZoom);
                    Point2D transformationPoint2 = getTransformedPoint(p);
                    transformation.translate(transformationPoint2.getX() - transformationPoint1.getX(), transformationPoint2.getY() - transformationPoint1.getY());
                    //update component
                    component.repaint();
                }
                //is the zoom already minimalZoom?
            } else if (startZoom > minimalZoom) {
                startZoom--;
                //apply transformation 
                Point2D p1 = getTransformedPoint(p);
                transformation.scale(factorZoom, factorZoom);
                Point2D p2 = getTransformedPoint(p);
                transformation.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY());
                //update component
                component.repaint();
            }
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        //update window
        ApplicationSingletone.getMainWindow().updateCursor();
    }

    /*Methods from Interface*/

    /*For documentation see the corresponding interface-class*/


    @Override
    public void mouseClicked(MouseEvent e) {
        //do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startDragPoint = e.getPoint();
        endDragPoint = null; //dragging has started
        ApplicationSingletone.getMainWindow().updateCursor();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Only move when shift tool is selected
        if (ApplicationSingletone.getMainWindow().getToolboxPanel().isShiftSelected()) {
            shiftView(e);
        }
        ApplicationSingletone.getMainWindow().updateCursor();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomView(e);
    }

    /*Setter and Getter*/

    private Point2D.Float getTransformedPoint(Point p1) throws NoninvertibleTransformException {
        //transform a single Point
        AffineTransform inverse = transformation.createInverse();
        Point2D.Float p2 = new Point2D.Float();
        inverse.transform(p1, p2);
        return p2;
    }

    public void setStartZoom(int zoomLevel) {
        this.startZoom = zoomLevel;
    }

    public AffineTransform getTransformation() {
        return transformation;
    }

    public void setTransformation(AffineTransform coordTransform) {
        this.transformation = coordTransform;
    }

}
