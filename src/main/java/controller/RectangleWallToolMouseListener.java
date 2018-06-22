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
 * Reacts to Mouse- and Keyboardactions to draw temporaryWall new recangular
 * wall on the map
 *
 * @author Pascal Andermatt, Jan Huber
 */
class RectangleWallToolMouseListener implements MouseListener, MouseMotionListener, Resetable {

    private final Map map; //the current user-map
    private Rectangle temporaryWall = null; //the wall that should be added to the map later

    private Point firstClick = null; //a wall is only added after the second click

    /**
     * Creates temporaryWall new BorderToolMouseListener
     */
    public RectangleWallToolMouseListener() {
        map = ApplicationSingletone.getCurrentMap();
        temporaryWall = null;
    }

    /**
     * Converts a given rectangle to a polygon. This code was found on the internet.
     *
     * @param rect the rectangle that should be converted
     * @return the polygon that was created
     * @see http://wikicode.wikidot.com/convert-rectangle-to-polygon
     */
    public static Polygon RectangleToPolygon(Rectangle rect) {
        //converts a rectangle to a polygon
        Polygon result = new Polygon();
        result.addPoint(rect.x, rect.y);
        result.addPoint(rect.x + rect.width, rect.y);
        result.addPoint(rect.x + rect.width, rect.y + rect.height);
        result.addPoint(rect.x, rect.y + rect.height);
        return result;
    }

    private void addFirstClick(MouseEvent e) {
        //play sound
        SoundPlayer.play("click3", "ogg");

        Map map = ApplicationSingletone.getCurrentMap();
        //set the location of the first click
        if (temporaryWall == null) {
            temporaryWall = new Rectangle(getConvertedMousePosition(e));
        }
        firstClick = e.getPoint();
    }

    private void addSecondClick(MouseEvent e) {
        //a second click is not allowd to have the same location as the first click
        if (e.getPoint().distance(firstClick) > 0) {
            //the temporaryWall should contains the location of both clicks
            temporaryWall.add(getConvertedMousePosition(e));
            Map map = ApplicationSingletone.getCurrentMap();

            //add a wall to the map
            boolean successful = map.addWall((RectangleToPolygon(temporaryWall)), false);
            if (successful) {
                //the sound is only played when the wall was sucessfully added
                SoundPlayer.play("click", "ogg");

            }

            //reset everything
            temporaryWall = null;
            firstClick = null;
            ApplicationSingletone.getCurrentMap().setTemporaryBorder(null);

        }

    }

    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {
        if (temporaryWall == null) {
            addFirstClick(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (temporaryWall == null) {
            addFirstClick(e);
        } else {
            addSecondClick(e);
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (temporaryWall == null) {
            return;
        }
        //display a preview for the user
        Rectangle tmp = new Rectangle(temporaryWall);
        tmp.add(getConvertedMousePosition(e));
        ApplicationSingletone.getCurrentMap().setTemporaryBorder(tmp);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if (temporaryWall == null) {
            return;
        }

        //display a preview for the user
        Rectangle tmp = new Rectangle(temporaryWall);
        tmp.add(getConvertedMousePosition(e));
        ApplicationSingletone.getCurrentMap().setTemporaryBorder(tmp);
    }

    @Override
    public void reset() {
        temporaryWall = null;
        firstClick = null;
    }

}
