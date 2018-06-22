package main.java.pedestrians;

import main.java.mapElements.Wall;
import main.java.math.RandomGenerator;
import main.java.pedestriansimulator.Map;

import java.awt.*;
import java.io.Serializable;

/**
 * An AbstractPedestrian contains all the attributes and methods which a
 * pedestrian-subclass should implement
 *
 * @author Pascal Andermatt, Jan Huber
 */
public abstract class AbstractPedestrian extends Drawable implements Cloneable, Serializable {

    //Static variables
    public final static double SQUARE_ROOT_OF_TWO = 1.41421356237; //= length of a diagonal step
    private final static int DEFAULT_RADIUS = 13;
    public Point currentLocation;
    public Point originLocation; //is not changed if the currentLocation changes
    public int radius;
    public int preferredSpace;
    private double speed;
    private double speedCounter;
    public boolean hasReachedTarget; //a pedestrian that has reached its target doesent move anymore
    public final PedestrianBehaviour behaviour; //contains information about how a pedestrian walks
    private final int stepsize;
    private Wall target = null;

    /**
     * Creates a new pedestrian
     *
     * @param color           the color that the pedestrian should have
     * @param currentLocation where on the map does the pedestrian stand?
     */
    AbstractPedestrian(Color color, Point currentLocation) {
        super(color);
        this.currentLocation = currentLocation;
        if (currentLocation != null) {
            this.originLocation = new Point(currentLocation.x, currentLocation.y);
        }
        //set default values
        radius = DEFAULT_RADIUS;
        //= default speed
        int DEFAULT_STEPSIZE = 1;
        stepsize = DEFAULT_STEPSIZE;
        preferredSpace = 30;
        speed = 1;
        speedCounter = 10;
        hasReachedTarget = false;
        behaviour = new PedestrianBehaviour(this);
    }

    /**
     * Returns if a pedestrian touches a given point.
     */
    public boolean doesTouch(Point p) {
        double distance = p.distance(getMiddlePoint());
        return (distance < radius);
    }

    /**
     * Returns if a pedestrian is touching another pedestrian
     */
    public boolean isTouchingPedestrian(AbstractPedestrian pedestrian) {
        if (hasReachedTarget || pedestrian.hasReachedTarget) {
            return false; //pedestirans that have reached their target can be ignored
        }
        //measure distance
        double distance = pedestrian.getCurrentLocation().distance(getCurrentLocation());
        return (distance < radius + pedestrian.radius);
    }

    /**
     * Returns if a pedestrian is inside the "private preferred space" of this pedestrian
     *
     * @param pedestrian
     * @return
     */
    public boolean isToNearToPedestrian(AbstractPedestrian pedestrian) {
        if (hasReachedTarget || pedestrian.hasReachedTarget) {
            return false; //pedestirans that have reached their target can be ignored
        }
        //measure distance
        double distance = pedestrian.getMiddlePoint().distance(getMiddlePoint());
        return (distance < (radius + preferredSpace + pedestrian.radius));
    }

    /**
     * Creates a copy of this pedestrian
     *
     * @return the cloned pedestrian
     */
    public AbstractPedestrian cloneThis() {
        try {
            return (AbstractPedestrian) super.clone();
        } catch (Exception e) {
            return null; //cloning not successful
        }
    }

    /**
     * Sets the current location of a pedestrian to its origin location
     */
    void resetLocation() {
        if (originLocation != null) { //set new current location
            currentLocation = new Point(originLocation.x, originLocation.y);
        }
        //reset other values
        setColor(target.getColor());
        hasReachedTarget = false;
        behaviour.stepsTakenBeforeDiagonalMove = 0;
    }

    /*Setter and Getter*/

    public Point getCurrentLocation() {
        return currentLocation;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isSelected() {
        return selected;
    }

    public Point getMiddlePoint() {
        return getCurrentLocation();
    }

    public void setCurrentLocationNotInternal(Point newCurrentLocation) {
        //sets both values: currentLocation and originLocation
        this.currentLocation = newCurrentLocation;
        this.originLocation = new Point(newCurrentLocation.x, newCurrentLocation.y);
    }

    public void internalSetCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getStepsize() {
        return stepsize;
    }

    public Wall getTarget() {
        return target;
    }

    public void setTarget(Wall goal) {
        if (goal == null) {
            setColor(RandomGenerator.randomBrightColor());
        }
        this.target = goal;
    }

    public void setPreferredSpace(int preferredSpace) {
        this.preferredSpace = preferredSpace;
    }

    public boolean hasReachedGoal(int tolerance, Map currentMap) {
        //overwrite in subclass
        return false;
    }

    public Point getNextGoalPoint(Map currentMap) {
        return null; //overwrite in Subclass
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeedCounter() {
        return speedCounter;
    }

    public void setSpeedCounter(double speedCounter) {
        this.speedCounter = speedCounter;
    }

    int distanceToGoal() {
        //overwrite in subclass
        return 0;
    }
}
