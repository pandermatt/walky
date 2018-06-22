package main.java.controller;

import main.java.gui.SoundPlayer;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static main.java.controller.PedestrianController.getConvertedMousePosition;

/**
 * Reacts to Mouse- and Keyboardactions to set a target for all the selected
 * pedestrians
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class MarkGoalToolMouseListener implements MouseListener, MouseMotionListener, Resetable {

    /**
     * Creates a new MarkGoalToolMouseListener
     */
    public MarkGoalToolMouseListener() {
    }

    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {
        //tell the map where the goal is
        boolean success = ApplicationSingletone.getCurrentMap().setGoalForSelectedPedestrians(getConvertedMousePosition(e));
        if (success) {
            //a new goal was set
            SoundPlayer.play("Munition3_minecraft", "wav");
            ApplicationSingletone.getMainWindow().getToolboxPanel().setSelectionTool();

        } else {
            //no goal was set
            SoundPlayer.play("Munition2_minecraft", "wav");

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
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //update mouse location
        Map map = ApplicationSingletone.getCurrentMap();
        map.setMouse(getConvertedMousePosition(e));
    }

    @Override
    public void reset() {
    }

}
