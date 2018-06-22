package main.java.pedestriansimulator;

import main.java.gui.ToolboxPanel;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import main.java.pedestrians.IntelligentPedestrian;
import main.java.pedestriansimulator.Map;

/**
 * The PedestrianAnimator is used to start an animation. As long as the animation
 * is running, the PedestrianAnimator tells each pedestrian to walk towards his target
 * @author Pascal Andermatt, Jan Huber
 */
public class PedestrianAnimator {

    //settings
    Map currentMap;
    int FRAMES_PER_SECOND = 130;
    private boolean wait;
    boolean isStopped;
    private int frameCount = 0;

    /**
     * Creates a new PedestrianAnimator.
     */
    public PedestrianAnimator(Map currentMap) {
        this.currentMap = currentMap;
        wait = false;
        isStopped = false;
        frameCount = 0;
    }

    /**
     * Creates a new PedestrianAnimator. Variable shouldWait can be choosen
     */
    public PedestrianAnimator(Map currentMap, boolean shouldWait) {
        this(currentMap);
        wait = shouldWait;
    }

    
    /**
     * Moves each pedestrian for a single step
     */
    public void moveAllPedestrians() {
        if (isStopped) return; //is the animation still running?
        currentMap.deselectAll(); //deselect all pedestrians during an animation
        ArrayList<IntelligentPedestrian> pedestrians = currentMap.getPedestrians();
            for (IntelligentPedestrian a : pedestrians) { //for each pedestrian...
                for (int i = 0; i < a.getStepsize(); i++) { //can pedestrian make a step?
                    if (a.getTarget() == null) {
                        a.behaviour.makeRandomStep(currentMap);
                    }
                    a.makeStep(currentMap); //tell pedestrian to make a step
                }
                
            }
            
            if (wait) {
                try {
            Thread.sleep(1000/FRAMES_PER_SECOND); //sleep is optional
        } catch (InterruptedException ex) {
            //sleep was interrupted
        }
            }
            
        frameCount++;
        if (currentMap.allPedestriansHaveReachedTarget()) {
            isStopped = true; //stop the animaiton
            
            //update toolbox panel
            ToolboxPanel toolbox = ApplicationSingletone.getMainWindow().getToolboxPanel();
            toolbox.animationPlay = false;
            toolbox.updateGUI();
            
            //show message
            JOptionPane.showMessageDialog(null, "All pedestrians have reached their target.\nTotal amount of steps taken: " + frameCount, "Animation finished", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void stop() {
        isStopped = true;
    }
}
