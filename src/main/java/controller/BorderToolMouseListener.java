package main.java.controller;


import main.java.gui.SoundPlayer;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static main.java.controller.PedestrianController.getConvertedMousePosition;

/**
 * Reacts to Mouse- and Keyboardactions to draw a new Border to the Map
 *
 * @author Pascal Andermatt, Jan Huber
 */
class BorderToolMouseListener implements MouseListener, MouseMotionListener, Resetable {

    private final static int THICKNESS = 2; //the thickness of the border's walls

    private final Map map; //the current user-map
    private boolean isDragging;
    private boolean isFirstClick; //a new border is added after the second click
    private Point click = null; //the location of a mouseClick

    /**
     * Creates a new BorderToolMouseListener
     */
    public BorderToolMouseListener() {
        isDragging = false;
        map = ApplicationSingletone.getCurrentMap();
        isFirstClick = true;
        click = null;
    }

    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {
        SoundPlayer.play("click3", "ogg");

        //React depending on the click-number
        if (isFirstClick) {
            isFirstClick = false;
            click = getConvertedMousePosition(e); //set first click location
        } else {
            map.wallBorderPoint = null;
            isFirstClick = true;
            addBorderFrom(click, getConvertedMousePosition(e)); //add the new Border

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Adds a border if the user had clicked and dragged
        if (isDragging || isFirstClick) {

            //add border
            addBorderFrom(click, getConvertedMousePosition(e));

            isFirstClick = true;
            map.wallBorderPoint = null;
        }
        isDragging = false;

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //display a temporary border to give the user a preview
        isDragging = true;
        Rectangle tmp = new Rectangle(click);
        tmp.add(getConvertedMousePosition(e));
        ApplicationSingletone.getCurrentMap().setTemporaryBorder(tmp);
        if (isFirstClick) {
            return;
        }

        isFirstClick = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //display a temporary border if the user has clicked but is not dragging.

        map.addBorderPoint(getConvertedMousePosition(e));
        if (isFirstClick) {
            return;
        }
        Rectangle tmp = new Rectangle(click);
        tmp.add(getConvertedMousePosition(e));
        ApplicationSingletone.getCurrentMap().setTemporaryBorder(tmp);

    }

    @Override
    public void reset() {
        //set everything to default again
        isDragging = false;
        ApplicationSingletone.getCurrentMap().setTemporaryBorder(null);
        isDragging = false;
        isFirstClick = true;
        click = null;
    }

    private void addBorderFrom(Point p1, Point p2) {

        //adds a border between two pooints
        SoundPlayer.play("click", "ogg");

        //a boarder consists of 4 individual walls which are merged together...
        addRectangleWall(new Point(p1.x - THICKNESS, p1.y + THICKNESS), new Point(p2.x + THICKNESS, p1.y - THICKNESS));
        addRectangleWall(new Point(p1.x + THICKNESS, p1.y - THICKNESS), new Point(p1.x - THICKNESS, p2.y + THICKNESS));
        addRectangleWall(new Point(p2.x + THICKNESS, p2.y + THICKNESS), new Point(p2.x - THICKNESS, p1.y - THICKNESS));
        addRectangleWall(new Point(p2.x + THICKNESS, p2.y + THICKNESS), new Point(p1.x - THICKNESS, p2.y - THICKNESS));
    }

    private void addRectangleWall(Point p1, Point p2) {
        //adds a single rectangle wall betwenn two points
        Rectangle r = new Rectangle(p1);
        r.add(p2);

        //add Wall to Map
        ApplicationSingletone.getCurrentMap().addWall(RectangleWallToolMouseListener.RectangleToPolygon(r), false);
        ApplicationSingletone.getCurrentMap().setTemporaryBorder(new Rectangle());
    }

}
