package main.java.controller;

import main.java.gui.PopUpMenu;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Is used to display a context-related list of actions after a
 * right-mouse-click from the user
 *
 * @author Pascal Andermatt, Jan Huber
 */
class RightclickMouseListener implements MouseListener, Resetable {

    /**
     * Creates a new RightclickMouseListener
     */
    public RightclickMouseListener() {
        //the current user-map
        Map map = ApplicationSingletone.getCurrentMap();

    }

    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {
        Map map = ApplicationSingletone.getCurrentMap();
        //If the click was a right-click, a context-related choice of actions should be shown
        if (e.getButton() == MouseEvent.BUTTON3) {
            map.mouseClicked(PedestrianController.getConvertedMousePosition(e));
            displayPopup(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
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
    public void reset() {
        boolean isDragging = false;
    }

    private void displayPopup(MouseEvent e) {
        Map map = ApplicationSingletone.getCurrentMap();
        map.wallPoints.clear();
        map.setSelection(new Polygon(), true);

        //Display a new PopUpMenu
        PopUpMenu menu = new PopUpMenu();
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
