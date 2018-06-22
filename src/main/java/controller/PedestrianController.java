package main.java.controller;

import main.java.gui.*;
import java.awt.*;
import java.awt.event.*;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

/**
 * Reacts to Mouse- and Keyboardactions and decides to which MouseListener the
 * event should be passed on
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class PedestrianController implements MouseListener, MouseMotionListener, KeyListener {

    /*All possible MouseListener*/
    RightclickMouseListener rightClickTool;
    BorderToolMouseListener borderTool;
    PedestrianMouseListener pedestrianTool;
    WallToolMouseListener wallTool;
    RectangleWallToolMouseListener squareTool;
    SelectionToolMouseListener selectionTool;
    MarkGoalToolMouseListener markGoalTool;

    /*The mouseListener that is currently selected*/
    MouseListener currentMouseListener;

    /*The current toolbox where the user can select a tool*/
    ToolboxPanel toolbox;

    /*contains the value of the last pressed key. 
     used for KeyboardActions*/
    Integer currentPressedKey;

    /**
     * Creates a new PedestrianController with a given ToolboxPanel to decide
     * which MouseListener should be used
     */
    public PedestrianController(ToolboxPanel toolbox) {
        this.toolbox = toolbox;
        toolbox.addKeyListener(this);
        toolbox.setFocusable(false);

        //initialize all MouseListener
        rightClickTool = new RightclickMouseListener();
        borderTool = new BorderToolMouseListener();
        pedestrianTool = new PedestrianMouseListener();
        wallTool = new WallToolMouseListener();
        squareTool = new RectangleWallToolMouseListener();
        selectionTool = new SelectionToolMouseListener();
        currentPressedKey = null;
        markGoalTool = new MarkGoalToolMouseListener();
    }

    private MouseListener getCurrentMouseListener(MouseEvent e) {

        //Returns the MouseListener that should be active
        currentMouseListener = null;

        //check which tool is selected in the toolbox
        if (e.getButton() == MouseEvent.BUTTON3 || e.isPopupTrigger()) {
            currentMouseListener = rightClickTool;
        } else if (toolbox.isBorderSelected()) {
            currentMouseListener = borderTool;
        } else if (toolbox.isPedestrianSelected()) {
            currentMouseListener = pedestrianTool;
        } else if (toolbox.isWallToolSelected()) {
            currentMouseListener = wallTool;
        } else if (toolbox.isSquareSelected()) {
            currentMouseListener = squareTool;
        } else if (toolbox.isShiftSelected()) {
        } else if (toolbox.isSelectionToolSelected()) {
            selectionTool.extendMode = (currentPressedKey != null && currentPressedKey == 16);
            currentMouseListener = selectionTool;

        } else if (toolbox.isMarkGoalSelected()) {
            currentMouseListener = markGoalTool;
        }

        //reset all the MouseListener that are not selected
        resetOthers();

        return currentMouseListener;
    }

    private MouseMotionListener getCurrentMouseMotionListener(MouseEvent e) {

        //Returns the MouseMotionListener that should be active
        currentMouseListener = null;

        //check which tool is selected in the toolbox
        if (toolbox.isBorderSelected()) {
            currentMouseListener = borderTool;
        } else if (toolbox.isPedestrianSelected()) {
            currentMouseListener = pedestrianTool;
        } else if (toolbox.isWallToolSelected()) {
            currentMouseListener = wallTool;
        } else if (toolbox.isSquareSelected()) {
            currentMouseListener = squareTool;
        } else if (toolbox.isSelectionToolSelected()) {
            selectionTool.extendMode = (currentPressedKey != null && currentPressedKey == 16);
            currentMouseListener = selectionTool;
        } else if (toolbox.isShiftSelected()) {
        } else if (toolbox.isMarkGoalSelected()) {
            currentMouseListener = markGoalTool;
        }
        //reset all the MouseMotionListener that are not selected

        resetOthers();

        //return-value must be casted because not every MouseListener is a MouseMotionListener too
        return (MouseMotionListener) currentMouseListener;
    }

    /*Implemented Methods. For documentation see the corresponding Interface-class e.g. MouseListener, MouseMotionListener or Resetable*/
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (toolbox.animationPlay) {
            //the user can't interact with the GUI during an animation
            return;
        }

        //Pass the event on the current mouseListener
        MouseListener currentListener = getCurrentMouseListener(e);
        if (currentListener != null) {
            currentListener.mouseClicked(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (toolbox.animationPlay) {
            //the user can't interact with the GUI during an animation

            return;
        }

        //Pass the event on the current mouseListener
        MouseListener currentListener = getCurrentMouseListener(e);
         //if the shift tool is selected, there is no mouselistener
        if (currentListener != null) {
            currentListener.mouseReleased(e);
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

        if (toolbox.animationPlay) {
            //the user can't interact with the GUI during an animation

            return;
        }

        //Pass the event on the current MouseMotionListener
        MouseMotionListener currentListener = getCurrentMouseMotionListener(e);
         //if the shift tool is selected, there is no mouselistener
        if (currentListener != null) {
            currentListener.mouseDragged(e);
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if (toolbox.animationPlay) {
            //the user can't interact with the GUI during an animation

            return;
        }

        //Tell the Map where the currentMouseLocation is
        Map map = ApplicationSingletone.getCurrentMap();
        map.setMouse(getConvertedMousePosition(e));
        MouseMotionListener currentListener = getCurrentMouseMotionListener(e);
        
        //if the shift tool is selected, there is no mouselistener
        if (currentListener != null) {
            currentListener.mouseMoved(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //All selected pedestrians should be removed if the user clicks the delete-key
        currentPressedKey = e.getKeyCode(); //get the current keycode
        if (e.getKeyCode() == 8 || e.getKeyCode() == 127) { //= Delete-Key...
            ApplicationSingletone.getCurrentMap().removeSelectedElements();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentPressedKey = null;
    }

    static Point getConvertedMousePosition(MouseEvent e) {
        //convert a given mousePosition by applying  the current scale and transformation
        return ApplicationSingletone.getCurrentMap().zoomListener.convertMousePosition(e.getPoint());
    }

    private void resetOthers() {
        //Reset all MouseListener's if they are not active
        resetIfNotEqual(rightClickTool);
        resetIfNotEqual(borderTool);
        resetIfNotEqual(pedestrianTool);
        resetIfNotEqual(wallTool);
        resetIfNotEqual(squareTool);
        resetIfNotEqual(selectionTool);

    }

    private void resetIfNotEqual(Resetable toReset) {
        //Resets a MouseListener if it is not the current MouseListener
        if (toReset != currentMouseListener) {
            toReset.reset();
        }
    }

}
