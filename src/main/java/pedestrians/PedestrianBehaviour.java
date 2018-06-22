package main.java.pedestrians;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import main.java.math.RandomGenerator;
import static main.java.pedestrians.AbstractPedestrian.SQUARE_ROOT_OF_TWO;
import main.java.pedestriansimulator.Map;

/**
 * The PedestrianBehaviour-Class defines how the pedestrians interact with each
 * other and how they decide the direction of their next step.
 */
public class PedestrianBehaviour implements Serializable{

    AbstractPedestrian pedestrian; //the pedestrian for which the next step should be calculated

    public double stepsUntilDiagonalMove; //after how many straight steps should the pedestrian do a diagonal step?
    public double stepsTakenBeforeDiagonalMove; //how many steps has the pedestrian taken befor he can do a diagonal step?

    /**
     * Creates a new PedestrianBehaviour
     */
    public PedestrianBehaviour(AbstractPedestrian pedestrian) {
        this.pedestrian = pedestrian;
        stepsTakenBeforeDiagonalMove = 0;
        stepsUntilDiagonalMove = 0;
    }

    /**
     * Returns if this pedestrian is stronger than another pedestrian.
     */
    public boolean hasPriorityTo(AbstractPedestrian otherPedestrian) {
        //Strength is defined by the distance to the target.
        //Pedestrians which are near their target become stronger
        return pedestrian.distanceToGoal() < otherPedestrian.distanceToGoal();
    }

    /**
     * Tell the pedestrian to make a step in a random direction.
     */
    public StepResult makeRandomStep(Map currentMap) {
        //Randomly generate the change in x- and y- direction
        int currentXChange = RandomGenerator.randomNumber(-1, 1);
        int currentYChange = RandomGenerator.randomNumber(-1, 1);

        Point newCoordinate = new Point(pedestrian.currentLocation.x + currentXChange,
                pedestrian.currentLocation.y + currentYChange);

        //Is the step possible?
        if (currentMap.isThisALegalPedestrianCoordinate(
                newCoordinate,
                pedestrian)) {

            //updat current pedestria-location
            pedestrian.currentLocation.x += currentXChange;
            pedestrian.currentLocation.y += currentYChange;

        } else {
            //step was not possible
            return new StepResult(true, 0);
        }
        return new StepResult(true, getStepLength(currentXChange, currentYChange));
    }

    /**
     * Tells the pedestrian to step towards a given Point.
     *
     * @param minimalDistancePoint the point towards which the pedestrian should
     * walk
     * @return
     */
    protected StepResult stepTowards(Point minimalDistancePoint, Map currentMap) {

        /**
         * A walking pedestrian has three priorities: 1. Keep Preffered space to
         * other pedestrians 2. Take the direct path 3. do a random step
         */
        //is the step possible?
        if (pedestrian.getSpeedCounter() < 1 || minimalDistancePoint == null) {
            return new StepResult(false, 0);
        }

        //speedCounter should not be larger than SQUARE_ROOT_OF_TWO
        if (pedestrian.getSpeedCounter() > SQUARE_ROOT_OF_TWO) {
            pedestrian.setSpeedCounter(SQUARE_ROOT_OF_TWO);
        }

        //are there pedestrians which are too near to the current pedestrian?
        ArrayList<AbstractPedestrian> collisions
                = currentMap.getColosionPedestrian(pedestrian.currentLocation, pedestrian);

        int xDifference = 0;
        int yDifference = 0;

        if (collisions.isEmpty()) {
            //calculate the changes in x- and y- direction to get nearer to the 
            //minimalDistancePoint

            //for x
            if (pedestrian.currentLocation.x == minimalDistancePoint.x) {
                xDifference = 0;
            } else if (pedestrian.currentLocation.x > minimalDistancePoint.x) {
                xDifference = -1;
            } else {
                xDifference = 1;
            }

            //for y
            if (pedestrian.currentLocation.y == minimalDistancePoint.y) {
                yDifference = 0;
            } else if (pedestrian.currentLocation.y > minimalDistancePoint.y) {
                yDifference = -1;
            } else {
                yDifference = 1;
            }

            //try to make the best step
            StepResult r = tryToMakeStep(xDifference, yDifference, currentMap, minimalDistancePoint, false);
            return r;
        } else {

            //there are collisions. Try to increase distance to other pedestrians
            //which direction would minimalize the collisions?
            //for x
            if (totalToNearDistance(
                    new Point(pedestrian.currentLocation.x + 1, pedestrian.currentLocation.y), collisions, currentMap) < totalToNearDistance(new Point(pedestrian.currentLocation.x, pedestrian.currentLocation.y), collisions, currentMap)) {
                xDifference = 1;
            } else {
                xDifference = - 1;
            }

            //for y
            if (totalToNearDistance(new Point(pedestrian.currentLocation.x, pedestrian.currentLocation.y + 1), collisions, currentMap) < totalToNearDistance(new Point(pedestrian.currentLocation.x, pedestrian.currentLocation.y), collisions, currentMap)) {
                yDifference = 1;
            } else {
                yDifference = - 1;
            }

            //try to make the best step
            StepResult r = tryToMakeStep(xDifference, yDifference, currentMap, minimalDistancePoint, true);
            return r;
        }
    }

    /**
     * Tries to walk towards a specified Point
     */
    private StepResult tryToMakeStep(int xDifference, int yDifference, Map currentMap, Point minimalPoint, boolean ignoreDiagonal) {

        //calculate the distance in x and y direction
        double xDistance = Math.abs(pedestrian.currentLocation.x - minimalPoint.x);
        double yDistance = Math.abs(pedestrian.currentLocation.y - minimalPoint.y);

        //stepsTakenBeforeDiagonalMove should not be smaller than 0
        if (stepsTakenBeforeDiagonalMove < 0) {
            stepsTakenBeforeDiagonalMove = 0;
        }

        //update stepsUntilDiagonalMove
        stepsUntilDiagonalMove = (IntelligentPedestrian.getLarger(xDistance, yDistance) / IntelligentPedestrian.getSmaller(xDistance, yDistance)) - 1;

        boolean yHasPriority = Math.abs(minimalPoint.y - pedestrian.currentLocation.y) > Math.abs(minimalPoint.x - pedestrian.currentLocation.x);

        //generate all possible steps
        Point newCoordinate1 = new Point(pedestrian.currentLocation.x + xDifference, pedestrian.currentLocation.y + yDifference);
        Point newCoordinate2 = new Point(pedestrian.currentLocation.x, pedestrian.currentLocation.y + yDifference);
        Point newCoordinate3 = new Point(pedestrian.currentLocation.x + xDifference, pedestrian.currentLocation.y);

        if (!yHasPriority) {
            //switch coordinates
            Point backup2 = newCoordinate2;
            newCoordinate2 = newCoordinate3;
            newCoordinate3 = backup2;
        }

        //no step should be taken
        if (xDifference == 0 && yDifference == 0) {
            return new StepResult(true, 0);
        }

        boolean shouldRecalculate = true;

        //check if newCoordinate1 is a legal coordinate
        if (currentMap.isThisALegalPedestrianCoordinate(newCoordinate1, pedestrian)) {

            if (xDifference == 0
                || yDifference == 0
                || ignoreDiagonal
                || stepsTakenBeforeDiagonalMove >= stepsUntilDiagonalMove) {

                //step would be possible
                if (canMakeStep(xDifference, yDifference)) {
                    //make step
                    return makeStep(newCoordinate1, ignoreDiagonal, currentMap); //schritt wie gewünscht
                } else {
                    //step would be possible but pedestrian waits to do a diagonal step later
                    return new StepResult(false, 0); //no step was taken
                }

            } else {
                //walking diagonaly is not possible yet
                shouldRecalculate = false;
            }
        }

        //check if possibility 2 is legal
        if (currentMap.isThisALegalPedestrianCoordinate(newCoordinate2, pedestrian) && !newCoordinate2.equals(pedestrian.currentLocation)) {
            return makeStep(newCoordinate2, shouldRecalculate, currentMap);
        }

        //check if possibility 3 is legal
        shouldRecalculate = true;
        if (currentMap.isThisALegalPedestrianCoordinate(newCoordinate3, pedestrian) && !newCoordinate3.equals(pedestrian.currentLocation)) {
            return makeStep(newCoordinate3, shouldRecalculate, currentMap); //oberes war nicht möglich
        }

        //no step was possible: make a random step
        return makeRandomStep(currentMap);

    }

    /**
     * Calculates how much other pedestrians are inside this pedestrian's 'preferred space'
     */
    double totalToNearDistance(Point location, ArrayList<AbstractPedestrian> pedestrians, Map currentMap) {
        double totalDistance = 0;
        //add distance for each pedestrian
        for (AbstractPedestrian a : pedestrians) {
            //only count pedestrians when they are visible
            if (a.getTarget() == null || pedestrian.getTarget() == null) {
                totalDistance += ((pedestrian.preferredSpace - Math.abs(location.distance(a.getCurrentLocation()))));

            }
            else if (currentMap.isVisible(location, a.getCurrentLocation(), a)) {
                totalDistance += ((pedestrian.preferredSpace - Math.abs(location.distance(a.getCurrentLocation()))) * (a.getTarget().equals(pedestrian.getTarget()) ? 1 : 100));
            }
        }

        return totalDistance;
    }

    /**
     * Makes a step without checking if it is legal
     */
    private StepResult makeStep(Point newCoordinate1, boolean recalculate, Map currentMap) {
        if (newCoordinate1.x == pedestrian.getCurrentLocation().x && (newCoordinate1.y == pedestrian.getCurrentLocation().y)) {
            //random step should be taken
            return makeRandomStep(currentMap);
        } else {
            double stepLength = getStepLength(newCoordinate1);
            //update current coordinate
            pedestrian.currentLocation = newCoordinate1;

            return new StepResult(recalculate, stepLength);
        }

    }
    
    /**
     * Checks if a step is possible.
     */
    private boolean canMakeStep(int xDifference, int yDifference) {
        return pedestrian.getSpeedCounter() >= getStepLength(xDifference, yDifference);
    }

    /*Setter and Getter*/
    
    /**
     * Calculates the length of a step for a given coordinate
     */
    private double getStepLength(Point goalPoint) {
        return getStepLength(goalPoint.x - pedestrian.currentLocation.x, goalPoint.y - pedestrian.currentLocation.y);
    }

     /**
     * Calculates the length of a step for a given x- and y- difference
     */
    private double getStepLength(int xDifference, int yDifference) {
        double both = Math.abs(xDifference) + Math.abs(yDifference);
        if ((both) == 2) {
            return SQUARE_ROOT_OF_TWO;
        }
        return both;
    }
}
