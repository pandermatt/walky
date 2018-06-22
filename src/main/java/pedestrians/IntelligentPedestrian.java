package main.java.pedestrians;

import main.java.algorithms.DijkstraAlgorithm;
import main.java.algorithms.Edge;
import main.java.algorithms.Node;
import main.java.gui.SoundPlayer;
import main.java.mapElements.Wall;
import main.java.math.NearestPointInLine;
import main.java.math.RandomGenerator;
import main.java.pedestriansimulator.Graph;
import main.java.pedestriansimulator.Map;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An IntelligentPedestrians calculates its fastest path to a target and tries
 * to reach the target as fast as possible
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class IntelligentPedestrian extends AbstractPedestrian implements Serializable {

    private final static int NO_FASTEST_PATH = -1;
    private ArrayList<Point> path; //the preferred path a pedestrians wants to take
    private boolean shouldUpdateFastestPath; //the fastest path will be regenerated if true
    private int nextTargetPoint; //the next point inside the path a pedestrian wants to reach

    /**
     * Creates a new IntelligentPedestrian on a given location
     */
    public IntelligentPedestrian(Point currentLocation) {
        super(RandomGenerator.randomBrightColor(), currentLocation);
        //initializes the variables
        path = new ArrayList<>();
        setSpeedCounter(0);
        shouldUpdateFastestPath = true;
    }

    /**
     * Creates a new IntelligentPedestrian and doesent initialize the attributes
     */
    public IntelligentPedestrian() {
        this(null);
    }

    /*Debuging Methods*/
    public static void deepPrint(Node p, int depth) {
        tabPrint(p.getPoint().toString(), depth);
        if (p.neighboursIsNull()) {
            p.setNeighbours(new Edge[0]);
        }

    }

    private static void tabPrint(String message, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
        System.out.print(message + "\n");
    }

    public static double getLarger(double xDistance, double yDistance) {
        return (xDistance >= yDistance ? xDistance : yDistance);
    }

    public static double getSmaller(double xDistance, double yDistance) {
        return (xDistance <= yDistance ? xDistance : yDistance);
    }

    /**
     * Tells the pedestrian that it can take a single step
     */
    public void makeStep(Map currentMap) {
        if (hasReachedTarget) {
            return; //a pedestrian that has reached the goal doesent have to make a step
        }

        //update speed counter
        setSpeedCounter(getSpeedCounter() + getSpeed());

        boolean stepTaken = true; //default value
        while (getSpeedCounter() >= 1 && stepTaken) {
            if (hasReachedTarget) {
                return; //a pedestrian that has reached the goal doesent have to make a step
            }

            if (shouldUpdateFastestPath) {
                //update path if necessarey
                nextTargetPoint = updateFastestPath(currentMap);
                if (nextTargetPoint == NO_FASTEST_PATH) {
                    return; //do nothing
                }
            }

            if (getPath().isEmpty()) {
                return; //there is no fastest path //do nothing
            }

            //make a step
            StepResult result = behaviour.stepTowards(path.get(nextTargetPoint), currentMap);

            //analyze the result of the step
            double stepLength = result.stepsize;
            stepTaken = result.stepsize > 0; //was a step taken
            setSpeedCounter(getSpeedCounter() - stepLength); //substract stepLength from the speedCpimter
            shouldUpdateFastestPath = result.recalculatePath;
            if ((result.stepsize == 0
                    && currentLocation.equals(path.get(path.size() - 1)))
                    || path.get(path.size() - 1).distance(currentLocation) <= radius + 1) {
                setColor(Color.BLACK);
                hasReachedTarget = true;
                SoundPlayer.play("reached", "wav");

            }
            if (result.stepsize == AbstractPedestrian.SQUARE_ROOT_OF_TWO) {
                //it was a diagonally step
                behaviour.stepsTakenBeforeDiagonalMove -= behaviour.stepsUntilDiagonalMove;
            } else {
                behaviour.stepsTakenBeforeDiagonalMove += result.stepsize;
            }
        }
    }

    /**
     * Recalculates the fastest path to the target
     */
    public int updateFastestPath(Map currentMap) {

        try {
            generateFastestPath(currentMap);
        } catch (Exception e) {
            System.err.println("Fastest Path konnte nicht berechnet werden");
            System.out.println("RandomStep");
            System.out.println("" + behaviour.makeRandomStep(currentMap).stepsize);
            e.printStackTrace(); //take a random step if there is no fastest path
        }

        //calculate which point to reach next
        int i = getNextGoalPointIndex(currentMap);

        //simplify path
        ArrayList<Object> toRemove = new ArrayList<>();
        toRemove.add(null);
        getPath().removeAll(toRemove); //Remove null-values from path

        if (!getPath().isEmpty()) {
            try {
                //which is the next point to take?

                //calculate diestnace
                double xDistance = Math.abs(currentLocation.x - getPath().get(i).x);
                double yDistance = Math.abs(currentLocation.y - getPath().get(i).y);

                //reset values for diagonal walking
                behaviour.stepsTakenBeforeDiagonalMove = 0;
                behaviour.stepsUntilDiagonalMove = (getLarger(xDistance, yDistance) / getSmaller(xDistance, yDistance)) - 1;
            } catch (Exception e) {
                System.err.println("Es gibt keinen schnellsten Weg");
                path.set(0, currentLocation);
                return NO_FASTEST_PATH; //no fastest path was found
            }
        }
        return i;
    }

    /**
     * Generates the fastest path from a given map
     */
    public void generateFastestPath(Map currentMap) {
        if (getTarget() == null) {
            return;
        }

        path.clear(); //clear the old path
        java.util.List<Node> fastestPath = new ArrayList<>();
        Graph graph = currentMap.buildGraph(this); //convert the map tp a graph
        Node currentLocationVertex = getCurrentVertex(currentMap, graph); //add own current location to the graph
        HashMap<Point, Point> nearGoalPoints = currentMap.getPointsWithDirectTargetConnection(this);

        //Run the DijkstraAlgorithm with different endPoints
        Double minimum = null;

        //for every goal Point
        for (Point singleGoalPoint: nearGoalPoints.keySet()) {

            //Setup the DijkstraAlgorithm
            DijkstraAlgorithm d = new DijkstraAlgorithm();
            d.setStartPoint(currentLocationVertex);

            //find the shortest path
            double currentMinimalDistance = Double.POSITIVE_INFINITY;
            try {
                currentMinimalDistance = graph.get(singleGoalPoint).minimalDistance;
            } catch (Exception e) {
                //no minimal value was found
            }

            if (minimum == null || currentMinimalDistance < minimum) {
                //a new minimal value was found
                minimum = currentMinimalDistance;
                fastestPath = d.createShortestPathToTarget(graph.get(singleGoalPoint));
            }
        }

        //add own currentLocation to the fastest path
        path.add(currentLocation);

        for (Node vertex: fastestPath) {
            if (vertex.getPoint().equals(currentLocation)) {
                continue;
            }
            path.add(vertex.getPoint());
        }

        //add target-point to fastest path
        path.add(nearGoalPoints.get(path.get(path.size() - 1)));

        //remove unused points inside the path
        simplifyPath(path, currentMap);
        //update the lastest point to reach
        simplyfyWithNearerEndPoint(path, currentMap);
        simplifyPath(path, currentMap); //again!

        //a direct path is always the shoftest one
        setDirectPathIfPossible(path, currentMap);

        //remove own location from the graph
        graph.remove(currentLocation);

        //remove own location from the path
        if ((path.size() > 1)) {
            if (path.get(0).equals(path.get(1))) {
                path.remove(0);
            }

        }

    }

    /**
     * Removes all the Points in a path that can be reached directly
     */
    private void simplifyPath(ArrayList<Point> path, Map currentMap) {

        //add currentLocation to path
        path.add(0, currentLocation);

        //loop through every point of the path
        for (int i = 0; i < path.size(); i++) { //from start to end
            for (int k = path.size() - 1; k > i + 1; k--) { //from end to start
                if (currentMap.isVisible(path.get(i), path.get(k), this)) {
                    //delete all Points between i and k
                    for (int deleteindex = k - 1; deleteindex > i; deleteindex--) {
                        path.remove(deleteindex);
                        k--;
                    }
                }
            }
        }
    }

    /**
     * deletes all Point in the fastest path from a given index to the last
     * index.
     */
    private void deleteFrom(int index) {
        ArrayList<Point> toRemove = new ArrayList<>();
        for (int i = index; i < path.size(); i++) {
            toRemove.add(path.get(i)); //add index to remove List
        }

        //remove all marked Points
        path.removeAll(toRemove);

    }

    /**
     * Sets the originalLocation of this pedestrian as its currentLocation and
     * resets other variables
     */
    @Override
    public void resetLocation() {
        super.resetLocation(); //-> currentLocation = originalLocation;
        path = new ArrayList<>();
        shouldUpdateFastestPath = true; //fastest path should be regenerated for the next step
        setSpeedCounter(0);
    }

    private void simplyfyWithNearerEndPoint(ArrayList<Point> path, Map currentMap) {

        if (path == null || path.isEmpty()) {
            return;
        }
        Point lastPoint = path.get(path.size() - 1);

        ArrayList<Point> convexGoal = getTarget().getAllPoints();

        int lastIndex = convexGoal.indexOf(lastPoint);

        int indexAfter = (lastIndex == convexGoal.size() - 1 ? 0 : lastIndex + 1);
        int indexBefore = (lastIndex == 0 ? convexGoal.size() - 1 : lastIndex - 1);

        for (int i = 0; i < path.size(); i++) {

            try {

                //ToDo: Hier nullPointerexpexption verhindern
                Point possibility1 = NearestPointInLine.minimalDistancePoint(path.get(i), convexGoal.get(lastIndex), convexGoal.get(indexAfter));
                Point possibility2 = NearestPointInLine.minimalDistancePoint(path.get(i), convexGoal.get(lastIndex), convexGoal.get(indexBefore));

                if (possibility2.distance(path.get(i)) < possibility1.distance(path.get(i))) { //ToDo: Hier nicht die Luftlinie nehmen
                    Point backup = possibility1;
                    possibility1 = possibility2;
                    possibility2 = backup;
                }

                if (currentMap.isVisible(path.get(i), possibility1, this)) {
                    if (path.get(i).distance(possibility1) > getDistanceFrom(i)) {
                        continue;
                    }

                    //i bis neu < i bis ende
                    deleteFrom(i + 1);
                    path.add(possibility1);
                    return;
                }

                if (currentMap.isVisible(path.get(i), possibility2, this)) {
                    if (path.get(i).distance(possibility2) > getDistanceFrom(i)) {
                        continue;
                    }

                    //i bis neu < i bis ende
                    deleteFrom(i + 1);
                    path.add(possibility2);
                    return;
                }

            } catch (Exception ignored) {
            }
        }

    }

    @Override
    public boolean hasReachedGoal(int tolerance, Map currentMap) {
        if (currentMap.isVisible(currentLocation, path.get(path.size() - 1), this)) {
            return currentLocation.distance(path.get(path.size() - 1)) <= tolerance;
        }
        return false;
    }

    private Integer getNextGoalPointIndex(Map currentMap) {
        //get next step

        if (path.size() == 2 && currentLocation.equals(path.get(0))) {
            return 1;
        }
        for (int i = path.size() - 1; i >= 0; i--) {
            //System.out.println("Für Pedestrian aufgerufen");
            if (currentMap.isVisible(currentLocation, path.get(i), this)) { //trough convex hull?
                /*if (currentLocation.distance(path.get(i)) <= (getRadius()+0)) {
                 continue;
                 }*/

                return i;
            }
        }
        return 0; //test
    }

    @Override
    int distanceToGoal() {
        int distance = 0;
        for (int i = 1; i < path.size(); i++) {
            distance += path.get(i - 1).distance(path.get(i));
        }

        return distance;

    }

    private Node getCurrentVertex(Map currentMap, Graph graph) {

        ArrayList<Edge> neighbours = new ArrayList<>();

        Node currentLocationVertex = new Node(currentLocation);

        HashMap<Point, Point> nearGoalPoints = currentMap.getPointsWithDirectTargetConnection(this);

        for (Point neighbour: currentMap.getAllVisiblePoints(currentLocation, this)) {
            //neighbour

            double distanceToNeighbour = Math.abs(currentLocation.distance(neighbour));

            if (nearGoalPoints.containsKey(neighbour)) {
                distanceToNeighbour += Math.abs(nearGoalPoints.get(neighbour).distance(neighbour));
            } else if (nearGoalPoints.containsKey(currentLocation)) {
                distanceToNeighbour += Math.abs(nearGoalPoints.get(currentLocation).distance(currentLocation));

            }

            //neighbours.add(new Edge(edges.get(neighbour), distanceToNeighbour));
            neighbours.add(new Edge(graph.get(neighbour), distanceToNeighbour)); //Evt hier
            currentLocationVertex.setNeighbours(Map.arrayFromList(neighbours));

        }

        return currentLocationVertex;

    }

    /*Setter and Getter*/
    public ArrayList<Point> getPath() {
        return path;
    }

    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }

    @Override
    public void setTarget(Wall goal) {
        super.setTarget(goal);

        if (goal != null) {
            if (!goal.equals(getTarget())) {
                hasReachedTarget = false;
            }
            setColor(goal.getOriginalColor());
            path = new ArrayList<>();
        }
    }

    @Override
    public Point getNextGoalPoint(Map currentMap) {
        return path.get(getNextGoalPointIndex(currentMap));
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    private double getDistanceFrom(int from) {
        double distance = 0;
        for (int i = from; i <= path.size() - 2; i++) {
            distance += path.get(i).distance(path.get(i + 1));
        }
        return distance;
    }

    private void setDirectPathIfPossible(ArrayList<Point> path, Map currentMap) {
        Point nearestPoint = currentMap.getDirectConnectionToGoal(currentLocation, this);

        if (nearestPoint != null) {
            try {
                if (nearestPoint.distance(currentLocation) < getDistanceFrom(0)) {
                    deleteFrom(1);
                    path.add(nearestPoint);
                }
            } catch (Exception e) {
                System.err.println("Distance: Null");
            }

        }
    }
}
