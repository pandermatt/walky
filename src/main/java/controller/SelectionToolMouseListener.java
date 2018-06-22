package main.java.controller;

import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static main.java.controller.PedestrianController.getConvertedMousePosition;

/**
 * Reacts to Mouse- and Keyboardactions to create or expand a selection
 *
 * @author Pascal Andermatt, Jan Huber
 */
class SelectionToolMouseListener implements MouseListener, MouseMotionListener, Resetable {

    private final Map map; //the current user-map
    boolean extendMode = false; //extend.mode is activated if the user presses the shift-key
    private Polygon currentSelection;

    /**
     * Creates a new SelectionToolMouseListener
     */
    public SelectionToolMouseListener() {
        currentSelection = new Polygon();
        map = ApplicationSingletone.getCurrentMap();

    }

    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {
        Map map = ApplicationSingletone.getCurrentMap();
        Point toAdd = getConvertedMousePosition(e);

        //set new selection
        map.setSelection(toAdd, extendMode);
        currentSelection.addPoint(toAdd.x, toAdd.y);
        map.change();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //update selection
        map.setSelection(new Polygon(), true);
        //remove current selection from the map
        currentSelection = new Polygon();
        map.change();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Add a new polygon-selection to enable more complex selections
        Point toAdd = getConvertedMousePosition(e);
        currentSelection.addPoint(toAdd.x, toAdd.y);
        map.setSelection(currentSelection, extendMode);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void reset() {
        currentSelection = new Polygon();
    }

}
