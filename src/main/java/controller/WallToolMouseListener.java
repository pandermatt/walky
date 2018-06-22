package main.java.controller;

import static main.java.controller.PedestrianController.getConvertedMousePosition;
import main.java.gui.SoundPlayer;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

public class WallToolMouseListener implements MouseListener, MouseMotionListener, Resetable {

    private final static int MINIMUM_DISTANCE = 40; //The minimal distance, the wall-Points should have

    ArrayList<Integer> polygonPointsX; //list of x-coordinates for the temporary wall
    ArrayList<Integer> polygonPointsY; //list of y-coordinates for the temporary wall
    Map map; //the current user-map
    private boolean isDragging = false;

    /**
     * Creates a new WallToolMouseListener
     */
    public WallToolMouseListener() {
        this.polygonPointsX = new ArrayList<>();
        this.polygonPointsY = new ArrayList<>();
        map = ApplicationSingletone.getCurrentMap();

    }

    private Polygon getConvertedPolygon() {
        //convert the x- and y-coordinates into a single polygon
        return new Polygon(
                arrayListToArray(polygonPointsX),
                arrayListToArray(polygonPointsY),
                polygonPointsX.size());
    }

    private void clearOldPoints() {
        //reset all values
        polygonPointsX.clear();
        polygonPointsY.clear();
        map.wallPoints.clear();
    }

    private static int[] arrayListToArray(ArrayList<Integer> integers) {
        //converts an arrayList with Integer-Values into an Array
        int[] returnArray = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < returnArray.length; i++) {
            //Loop through every single Integer
            returnArray[i] = iterator.next().intValue();
        }
        return returnArray;
    }

    private void addNewPolygonPoint(Point toAdd, boolean ignoreDistance) {

        //only add a new polygon-point if the distance to the last added point is larger than the MINIMUM_DISTANCE
        if (!ignoreDistance && !polygonPointsX.isEmpty()) {
            int lastX = polygonPointsX.get(polygonPointsX.size() - 1);
            int lastY = polygonPointsY.get(polygonPointsX.size() - 1);
            if (new Point(lastX, lastY).distance(toAdd) <= MINIMUM_DISTANCE) {
                return;
            }
        }

        //add points
        map.addWallPoint(toAdd);
        polygonPointsX.add(toAdd.x);
        polygonPointsY.add(toAdd.y);
    }


    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {

        //play sound
        SoundPlayer.play("click3", "ogg");

        //was it a double click?
        if (e.getClickCount() != 2) {
            //it was a double click -> add new Wall
            Point toAdd = new Point(getConvertedMousePosition(e));
            if (polygonPointsX.isEmpty()
                || polygonPointsX.get(polygonPointsX.size() - 1) != toAdd.x
                || polygonPointsY.get(polygonPointsY.size() - 1) != toAdd.y) {
                //add the last point if it was not already added
                addNewPolygonPoint(toAdd, true);

            }
        } else {
            if (polygonPointsX.size() > 2) { //a wall has to contain at least 3 points
                //wall should be added...
                //convert the wall-Points into a single polygon
                boolean success = map.addWall(getConvertedPolygon(), true);
                if (success) {
                    //play sound if wall was added
                    SoundPlayer.play("click", "ogg");

                }
            }
            //reset all values
            clearOldPoints();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging) {

            //add wall if the user was dragging
            boolean success = map.addWall(getConvertedPolygon(), true);

            clearOldPoints();
            if (success) {
                //play sound if wall was sucessfully added
                SoundPlayer.play("click", "ogg");

            }

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

        isDragging = true;
        Point toAdd = new Point(getConvertedMousePosition(e));
        addNewPolygonPoint(toAdd, false);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void reset() {
        //reset all values
        this.polygonPointsX = new ArrayList<>();
        this.polygonPointsY = new ArrayList<>();
        isDragging = false;
    }

}
