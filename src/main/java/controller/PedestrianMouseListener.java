package main.java.controller;

import static main.java.controller.PedestrianController.getConvertedMousePosition;
import main.java.gui.SoundPlayer;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JOptionPane;
import main.java.pedestrians.AbstractPedestrian;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

/**
 * Reacts to Mouse- and Keyboardactions to add new pedestrians
 *
 * @author Pascal Andermatt, Jan Huber
 */
class PedestrianMouseListener implements MouseListener, MouseMotionListener, Resetable {

    Map map; //the current user-map

    /**
     * Creates a new PedestrianMouseListener
     */
    public PedestrianMouseListener() {
        map = ApplicationSingletone.getCurrentMap();
    }

    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {
        //add a new Pedestrian
        addPedestrians(e);

    }

    public void addPedestrians() {

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

        addPedestrians(e);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void reset() {
    }

    private void addPedestrians(MouseEvent e) {
        boolean generalSuccess = false;

        Map map = ApplicationSingletone.getCurrentMap();

        //With the 'brush-tool', more than one pedestrian can be added at the same time
        //get brush-size
        int pedestriansPerLine = ApplicationSingletone.getMainWindow().getToolboxPanel().pedDrawSize();

        //get values of the pedestrians that should be added
        AbstractPedestrian pedestrian = ApplicationSingletone.getPedestrianPanel().getPedestrian();
        int radius = pedestrian.getRadius();
        int preferred = pedestrian.preferredSpace;

        //add every pedestrian
        for (int i = 0; i < pedestriansPerLine; i++) {
            for (int j = 0; j < pedestriansPerLine; j++) {
                //the total radius that a pedestrian should have before the next pedestrian gets added
                int totalSpace = radius + preferred;

                //calculate x and y location
                int toAddX = getConvertedMousePosition(e).x - (totalSpace * pedestriansPerLine) + i * totalSpace * 2 + totalSpace;
                int toAddY = getConvertedMousePosition(e).y - (totalSpace * pedestriansPerLine) + j * totalSpace * 2 + totalSpace;
                Point toAddPoint = new Point(toAddX, toAddY);

                //only add pedestrians with a lega coordinate
                if (map.isLegalPedestrianCoordinate(toAddPoint, radius)) {
                    //add a pedestrian
                    boolean success = map.addPedestrian(toAddPoint);

                    if (success) {
                        generalSuccess = true;
                    }
                }
            }
        }

        if (generalSuccess) { //at least one pedestrian was added
            SoundPlayer.play("Click_Soft_00", "wav");
        }
    }

}
