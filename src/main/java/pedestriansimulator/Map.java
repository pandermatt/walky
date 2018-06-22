package main.java.pedestriansimulator;

import main.java.algorithms.Edge;
import main.java.algorithms.Node;
import main.java.gui.SoundPlayer;
import main.java.gui.ZoomMouseListener;
import main.java.mapElements.Tree;
import main.java.mapElements.Wall;
import main.java.math.NearestPointInLine;
import main.java.pedestrians.AbstractPedestrian;
import main.java.pedestrians.IntelligentPedestrian;
import main.java.polygonAlgorithms.PolygonHelper;
import main.java.polygonAlgorithms.PolygonIntersectionChecker;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * The Map is the model of the simulation. It contains all the walls,
 * pedestrians etc.
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Map extends Observable implements Serializable {

    //the list with the pedestrians on their origin locatin. Used to reset the location
    private ArrayList<IntelligentPedestrian> pedestriansClone;
    //all the other elements
    private final ArrayList<Wall> walls;
    private final ArrayList<Tree> trees;
    //temporary wall and border-previews
    public transient Point wallBorderPoint;
    private Rectangle temporaryBoarder = new Rectangle();
    public transient ArrayList<Point> wallPoints;
    //the current zoomListener for the view
    public transient ZoomMouseListener zoomListener = new ZoomMouseListener(null);
    //a list of all the pedestrians
    private ArrayList<IntelligentPedestrian> pedestrians;
    //animation-variables
    private boolean isAnimated;
    private transient Thread animationThread;
    //the current mouse position
    private transient Point mousePosition;
    //the current selection-polygon
    private transient Polygon selection;

    /**
     * Creates a new Map
     */
    public Map() {
        //initialize all variables
        pedestrians = new ArrayList<>();
        walls = new ArrayList<>();
        wallPoints = new ArrayList<>();
        //wallSquare = null;
        trees = new ArrayList<>();
        wallBorderPoint = null;
        isAnimated = false;
        pedestriansClone = new ArrayList<>();
        selection = new Polygon();
        temporaryBoarder = new Rectangle();
        //reset the cache
        clearWallCache();
    }

    /**
     * Converts a list of edges into an array of edges
     *
     * @param edges the list that should be converted
     * @return the converted list as an array
     */
    public static Edge[] arrayFromList(ArrayList<Edge> edges) {
        Edge[] returnValue = new Edge[edges.size()];

        //add every Edge
        for (int i = 0; i < edges.size(); i++) {
            returnValue[i] = edges.get(i);
        }
        return returnValue;
    }

    /**
     * Resets all the transistant variables to default-values. This method is
     * called after a map was imported from a File
     */
    public void setupAfterImport() {
        wallPoints = new ArrayList<>();
        wallBorderPoint = null;
        mousePosition = new Point(-100, -100);
        pedestriansClone = new ArrayList<>();
        selection = new Polygon();
        temporaryBoarder = new Rectangle();
        clearWallCache();

        //Tell the observers that the map has changed
        ApplicationSingletone.mapHasChanged();

    }

    /**
     * adds a new pedestrian to the map
     *
     * @param pedestrian the pedestrian to add
     * @return was the pedestrian successfully added?
     */
    public boolean addPedestrian(Point pedestrian) {
        IntelligentPedestrian toAdd = ApplicationSingletone.getPedestrianPanel().getPedestrian();
        toAdd.setCurrentLocationNotInternal(pedestrian);
        if (isThisALegalPedestrianCoordinate(toAdd.getCurrentLocation(), toAdd)) {
            //Add pedestrian if legal
            pedestrians.add(toAdd);
            setChanged();
            notifyObservers();
            return true;
        }
        return false;

    }

    /**
     * Returns if a given location would be a legal position for a pedestrian
     *
     * @param location the location to check
     * @param radius   the radius of the pedestrian
     * @return
     */
    public boolean isLegalPedestrianCoordinate(Point location, int radius) {
        IntelligentPedestrian toAdd = new IntelligentPedestrian(location);
        toAdd.setRadius(radius);
        //check if the pedestrian has a legal coordinate
        return (isThisALegalPedestrianCoordinate(toAdd.getCurrentLocation(), toAdd));
    }

    /**
     * Adds a wall without checking if the coordinates are legal.
     *
     * @param toAdd
     */
    public synchronized void addWithoutCheck(Wall toAdd) {
        //update wall
        walls.add(toAdd);
        clearWallCache();
        change();
    }

    /**
     * Adds a new wall no the map.
     *
     * @param wall the wall to add
     * @return was the wall successully added?
     */
    public synchronized boolean addWall(Polygon wall, boolean checkEdge) {
        ArrayList<AbstractPedestrian> toRemove2 = new ArrayList<>();
        for (AbstractPedestrian a: getPedestrians()) {
            if (a.hasReachedTarget) {
                toRemove2.add(a);
            }
        }
        getPedestrians().removeAll(toRemove2);
        //all walls must have at least 2 Points
        if (wall.npoints <= 2) {
            return false;
        }
        //Self-Intersecting Polygons can not be added
        if (!PolygonIntersectionChecker.isSelfIntersecting(wall)) {
            JOptionPane.showMessageDialog(null, "Adding Polygon is not possible becauseit is self-intersecting.", "Polygon not allowed", JOptionPane.OK_OPTION);
            change();
            return false;
        }

        //Measure all the angles of a wall
        ArrayList<Point> points = PolygonHelper.getPointsFromPolygon(wall);
        ArrayList<Point> newPolygon = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            //angles that are too small can cause problems in creating a convex hull
            if (checkEdge) {
                int indexBefore = i == 0 ? points.size() - 1 : i - 1;
                int indexAfter = i == points.size() - 1 ? 0 : i + 1;
                double angle = Math.abs(angleBetween(points.get(i), points.get(indexBefore), points.get(indexAfter)));
                if (angle <= 20) {
                    //angle is to small: cut the dege

                    //caluculate the x and y that should be added to the edge
                    int xDifference = points.get(indexAfter).x - points.get(indexBefore).x;
                    xDifference = getDirection(xDifference);
                    int yDifference = points.get(indexAfter).y - points.get(indexBefore).y;
                    yDifference = getDirection(yDifference);
                    Point toAdd1 = new Point(points.get(i).x + xDifference, points.get(i).y + yDifference);

                    //do the same calculation for the second edge
                    int xDifference2 = points.get(indexBefore).x - points.get(indexAfter).x;
                    xDifference2 = getDirection(xDifference2);
                    int yDifference2 = points.get(indexBefore).y - points.get(indexAfter).y;
                    yDifference2 = getDirection(yDifference2);

                    //new edge point
                    Point toAdd2 = new Point(points.get(i).x + xDifference2, points.get(i).y + yDifference2);

                    //add the new EdgePoints
                    newPolygon.add(toAdd1);
                    newPolygon.add(toAdd2);

                } else {
                    //add the edge-Point
                    newPolygon.add(points.get(i));
                }

            } else {
                //add the edge-Point
                newPolygon.add(points.get(i));
            }
        }

        //Convert the Points to a Polygon
        Polygon newToAdd = new Polygon();
        for (Point point: newPolygon) {
            newToAdd.addPoint(point.x, point.y);
        }

        //Convert the Polygon to a wall
        Wall toAdd = new Wall(newToAdd);

        //get all pedestrians that are under the wall
        ArrayList<AbstractPedestrian> toRemove = new ArrayList<>();
        for (IntelligentPedestrian pedestrian: pedestrians) {
            AbstractPedestrian current = pedestrian;
            if (toAdd.intersectsLines(current) || wall.contains(current.getCurrentLocation())) {
                toRemove.add(pedestrian);
                //addWall(newToAdd);
            }
        }

        pedestrians.removeAll(toRemove);

        //remove all pedestrians that are under the wall
        ArrayList<Wall> removeWalls = new ArrayList<>();
        ArrayList<AbstractPedestrian> targetPedestrians = new ArrayList<>();

        for (Wall wall1: walls) {
            //get all walls that intersects the new Wall
            if (toAdd.intersectsWall(wall1)) {
                //merge walls
                toAdd.merge(wall1);
                for (AbstractPedestrian a: getPedestrians()) {
                    if (a.getTarget() != null && a.getTarget().equals(wall1)) {
                        targetPedestrians.add(a);
                    }
                }
                //remove existing wall
                removeWalls.add(wall1);
            }

        }

        //remove all walls that were merged
        walls.removeAll(removeWalls);

        //add the new wall
        walls.add(toAdd);

        //clear any caches
        clearWallCache();

        for (AbstractPedestrian a: targetPedestrians) {
            a.setTarget(toAdd);
        }

        //notify observers
        generateAllFastestPath();
        change();
        return true;
    }

    /**
     * Tells all the pedestrians to recalculate their fastest path
     */
    public void generateAllFastestPath() {
        for (AbstractPedestrian p: pedestrians) {
            //for all pedestrians
            if (p instanceof IntelligentPedestrian) {
                IntelligentPedestrian p2 = (IntelligentPedestrian) p;
                if (p2.getTarget() != null) { //target may be null
                    p2.generateFastestPath(this); //generate the fastest path
                    p2.updateFastestPath(this);
                }

            }
        }
    }

    /**
     * Adds a single point to the temporary wall
     *
     * @param toAdd the point to add
     */
    public void addWallPoint(Point toAdd) {
        wallPoints.add(toAdd);
        setChanged();
        notifyObservers();
    }

    /**
     * Adds a single point to the temporary border
     *
     * @param toAdd
     */
    public void addBorderPoint(Point toAdd) {
        wallBorderPoint = toAdd;
        setChanged();
        notifyObservers();
    }

    /**
     * tells the map where the current MouseLocaiton is
     *
     * @param mouseLocation the current location of the mouse
     */
    public void setMouse(Point mouseLocation) {
        mousePosition = mouseLocation;
        setChanged();
        notifyObservers();

    }

    /**
     * An animation starts in a new thread. All pedestians are constantly called
     * to make their next step.
     */
    public void startAnimation() {
        createPedestrianClone();
        isAnimated = true;
        final Map testMap = this;
        final PedestrianAnimator animator = new PedestrianAnimator(this);
        //create a new thread
        animationThread = new Thread(() -> {
            //pedestrians should constantly move
            while (true) {
                animator.moveAllPedestrians();
                testMap.change();
            }
        });

        //start the animation
        animationThread.start();

    }

    /**
     * Stops an animation if running
     *
     * @deprecated
     */
    public void stopAnimation() {
        isAnimated = false;
        animationThread.stop();
    }

    /**
     * Notifies all observers about a change in the map
     */
    public void change() {
        setChanged();
        notifyObservers();

    }

    /**
     * Loads a hardcoded wall to the map
     */
    public void addStation() {
        walls.clear();

        //get screen dimensions
        final int screenWidth = ApplicationSingletone.getMainWindow().getWidth();
        final int screenHeight = ApplicationSingletone.getMainWindow().getHeight();

        //save all the coordinates for x
        int[] xCoordinates = {
                22, screenWidth - 22,
                screenWidth - 22,
                screenWidth / 4 * 3,
                screenWidth / 4 * 3,
                screenWidth / 4,
                screenWidth / 4,
                22};

        //save all the coordinates for y
        int[] yCoordinates = {
                0,
                0,
                screenHeight / 10,
                screenHeight / 10,
                screenHeight / 10 * 4,
                screenHeight / 10 * 4,
                screenHeight / 10,
                screenHeight / 10};

        //add the new wall
        addWall(new Polygon(xCoordinates, yCoordinates, 8), true);

        //save the coordinates for the second wall
        int[] yCoordinates2 = {
                screenHeight,
                screenHeight,
                screenHeight - screenHeight / 10,
                screenHeight - screenHeight / 10,
                screenHeight / 10 * 6,
                screenHeight / 10 * 6,
                screenHeight - screenHeight / 10,
                screenHeight - screenHeight / 10};
        addWall(new Polygon(xCoordinates, yCoordinates2, 8), true);

        //clear the cache
        clearWallCache();
    }

    /**
     * Selects all the pedestrians on a given point
     *
     * @param selectionPoint the current mouseLocation
     * @param extendMode     true if the selection schould be expanded
     */
    public void setSelection(Point selectionPoint, boolean extendMode) {
        for (AbstractPedestrian a: pedestrians) { //for every pedestrian
            if (a.doesTouch(selectionPoint)) {
                if (extendMode) {
                    //extend selection
                    a.setSelected(!a.isSelected());
                } else {
                    //change selection
                    a.setSelected(true);
                }
            } else {
                if (!extendMode) {
                    //deselect
                    a.setSelected(false);
                }
            }
        }

        for (Wall a: walls) { //for every wall
            if (a.doesTouch(selectionPoint)) {
                //change selection
                if (extendMode) {
                    //extend selection
                    a.setSelected(!a.isSelected());
                } else {
                    //change selection
                    a.setSelected(true);
                }
            } else {
                if (!extendMode) {
                    //deselect
                    a.setSelected(false);
                }
            }
        }
    }

    /**
     * Tells the map that the mouse was clicked on a given location
     *
     * @param point the position where the mouse was clicked
     * @return true is something was selected
     */
    public void mouseClicked(Point point) {
        boolean wasSelected = false;
        //select pedestrians if necessary
        for (AbstractPedestrian a: pedestrians) {
            if (a.doesTouch(point)) {
                wasSelected = true;
            }
            a.setSelected(a.doesTouch(point));
        }

        //select walls if neccessary
        for (Wall wall: walls) {
            wall.setSelected(wall.doesTouch(point));
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Tells if the map has selected elements
     *
     * @return true if there are selected elements, otherwise false
     */
    public boolean hasSelectedElements() {
        return hasSelectedPedestrians() || hasSelectedWalls();
    }

    /**
     * Tells if a given location would be a legal location for a pedestrian
     *
     * @param check  the location to check
     * @param whoAmI for which pedestrian should the location be checked
     * @return true if the location is legal, otherwise false
     */
    public boolean isThisALegalPedestrianCoordinate(Point check, AbstractPedestrian whoAmI) {

        //clone the pedestrian
        AbstractPedestrian clone = whoAmI.cloneThis();

        clone.internalSetCurrentLocation(check);

        //for every pedestrian
        for (AbstractPedestrian a: pedestrians) {
            if (a.equals(whoAmI)) { //the new location may intersect the 'old' pedestrian
                continue;
            }
            if (a.getRadius() <= 0) {
                continue;
            }
            if (a.isTouchingPedestrian(clone)) {
                ///it is not a legal location because it would intersect other pedestrians
                return false;
            }
        }
        //check if walls would be intersected
        return doesPedestrianTouchWall(check, whoAmI);

    }

    /**
     * Updates the current selection
     *
     * @param currentSelectionPolygon the new selection
     * @param extend                  true if the extend-mode is activated - no pedestrian will
     *                                be deselected. Otherwise false
     */
    public void setSelection(Polygon currentSelectionPolygon, boolean extend) {
        selection = currentSelectionPolygon;
        //for every pedestrian
        for (AbstractPedestrian a: pedestrians) {
            //does the selection contain the current pedestrian
            boolean contains = currentSelectionPolygon.contains(a.getMiddlePoint());
            if (extend) {
                if (contains) {
                    //change the selection of the pedestrian
                    a.setSelected(!a.isSelected());
                }
            } else {
                //mark the pedestrian as selected if it is inside the selection
                a.setSelected(contains);
            }

            if (!(extend && a.isSelected())) {
                //mark the pedestrian as selected if it is inside the selection
                a.setSelected(contains);

            }
        }
        //notify observers
        change();
    }

    /**
     * Removes all elements on the map
     */
    public void clear() {
        //play sound
        SoundPlayer.play("remove", "ogg");

        //remove all elements
        walls.clear();
        pedestrians.clear();
        wallPoints.clear();
        pedestriansClone.clear();
        trees.clear();

        //remove cache
        clearWallCache();

        //notify observers
        change();
    }

    /*  //adds a new Border-Wall
     public void addBorder(int height, int wight) {
     border.addWallFrame(wight, height); //wall
     clearWallCache();
     }*/

    /**
     * Clears a temporary selection
     */
    public void clearPoints() {
        wallPoints.clear();
        change();
    }

    /**
     * Removes all selected elements on the map
     */
    public void removeSelectedElements() {
        //play sound
        SoundPlayer.play("remove", "ogg");
        //get all pedestrians that should be removed
        ArrayList<AbstractPedestrian> removePedestrians = new ArrayList<>();
        for (IntelligentPedestrian pedestrian: pedestrians) {
            if (pedestrian.isSelected()) {
                removePedestrians.add(pedestrian);
            }
        }

        //get all walls that should be removed
        ArrayList<Wall> removeWalls = new ArrayList<>();
        for (Wall wall: walls) {
            if (wall.isSelected()) {
                removeWalls.add(wall);
            }
        }

        //remove all pedestrians and walls
        pedestrians.removeAll(removePedestrians);
        removeWalls(removeWalls);

        //notify observer
        clearWallCache();
        change();
    }

    /**
     * removes all the walls of a given list
     *
     * @param toRemove a list of walls to remove
     */
    private void removeWalls(ArrayList<Wall> toRemove) {
        for (Wall w: toRemove) {
            for (AbstractPedestrian a: pedestrians) {
                if (w.equals(a.getTarget())) {
                    a.setTarget(null);
                    IntelligentPedestrian p = (IntelligentPedestrian) a;
                    p.setPath(new ArrayList<>());
                }

            }
        }
        walls.removeAll(toRemove);
        generateAllFastestPath();
    }

    /**
     * Adds a list of trees to the map
     */
    public void addAllTrees(ArrayList<Tree> newTrees) {
        trees.addAll(newTrees);
        //notify observer
        change();
    }

    /**
     * Tells all pedestrians that the selected wall is the new targed
     */
    public void markSelectedWallsAsTarget() {
        Wall selected = null;

        //which wall is selected
        for (Wall w: walls) {
            if (w.isSelected()) {
                selected = w;
                break;
            }
        }

        //update the target for every pedestrians
        for (AbstractPedestrian p: pedestrians) {
            p.setTarget(selected);
        }

        //regenerate the fastest path
        generateAllFastestPath();

        //notify observer
        change();
    }

    /**
     * Returns a list of all visible edges on the map
     *
     * @param currentPedestrian the list may be different for pedestrians with a
     *                          different radius
     * @return
     */
    public synchronized ArrayList<Point> getAllEdges(AbstractPedestrian currentPedestrian) {

        //create parameters for caching
        String methodName = "getAllEdges";
        String unique = "" + currentPedestrian.getRadius();

        //is a result already cached
        Object result = Cacher.get(methodName, unique);

        if (result != null) {
            //there is already a result...
            ArrayList<Point> list = (ArrayList<Point>) result;
            //clone the result
            return new ArrayList<>(list);
        }

        //no result was cached
        ArrayList<Point> edges = new ArrayList<>();
        //get all edges
        for (Wall wall: getWalls()) {
            edges.addAll(PolygonHelper.getPointsFromPolygon(wall.getSingleConvexHull(currentPedestrian)));
        }

        //clone result
        ArrayList<Point> edgeCopy = new ArrayList<>(edges);

        //store result
        Cacher.store(methodName, unique, edges);

        return edgeCopy;
    }

    /**
     * Returns all the points that are visible for a given pedestrian on a given
     * location
     *
     * @param currentLocation the location to check if a point is visible
     * @param pedestrian      the pedestrian for which should be checked if a point
     *                        is visible
     * @return a list of visible points
     */
    public ArrayList<Point> getAllVisiblePoints(Point currentLocation, AbstractPedestrian pedestrian) {
        ArrayList<Point> points = new ArrayList<>();
        //create a dummy pedestrian
        AbstractPedestrian dummy = new IntelligentPedestrian(currentLocation);
        //update dummy-Radius
        dummy.radius = pedestrian.getRadius();

        //get all edges for a pedestrian
        for (Point p: getAllEdges(dummy)) {
            if (isVisible(currentLocation, p, pedestrian)) {
                points.add(p);
            }
        }

        //if a pedestrian has no visible points
        if (points.isEmpty()) {
            System.out.println("Point is empty");
        }

        return points;
    }

    /**
     * A shortcut for the internal method "is visible"
     */
    public boolean isVisible(Point currentLocation, Point point, AbstractPedestrian pedestrian) {
        return isVisible(currentLocation, point, pedestrian, null);
    }

    /**
     * Converts the map into a graph for a given pedestrian
     *
     * @param pedestrian the pedestrian for which the graph should be created
     * @return this map as a graph
     */
    public Graph buildGraph(AbstractPedestrian pedestrian) {

        //create caching-parameters
        String methodName = "buildGraph";
        String unique = "" + pedestrian.getRadius() + pedestrian.getTarget();

        //get caching-result
        Object result = Cacher.get(methodName, unique);

        //is there a result?
        if (result != null) {
            Graph r = (Graph) result;
            //return the cloned result
            return r.clone();
        }

        //build a new graph
        Graph edges = new Graph();

        //get all points that have a direct conection to the goal
        HashMap<Point, Point> nearGoalPoints = getPointsWithDirectTargetConnection(pedestrian);

        //convert all edges into a node
        for (Point p: getAllEdges(pedestrian)) {
            edges.add(p, new Node(p));
        }

        //Loop through every edge
        for (Point p: getAllEdges(pedestrian)) {
            //search all neighbours tat should be connected to a node
            ArrayList<Edge> neighbours = new ArrayList<>();
            for (Point neighbour: getAllVisiblePoints(p, pedestrian)) {
                //calculate the distance between the two nodes
                double distanceToNeighbour = Math.abs(p.distance(neighbour));
                if (nearGoalPoints.containsKey(neighbour)) {
                    distanceToNeighbour += Math.abs(nearGoalPoints.get(neighbour).distance(neighbour));
                } else if (nearGoalPoints.containsKey(p)) {
                    distanceToNeighbour += Math.abs(nearGoalPoints.get(p).distance(p));
                }

                //use the distance as the "weight" of an edge
                neighbours.add(new Edge(edges.get(neighbour), distanceToNeighbour));
                edges.get(p).setNeighbours(arrayFromList(neighbours));
            }
        }

        //cache the result
        Cacher.store(methodName, unique, edges);

        //return a cloned result
        return edges.clone();
    }

    /**
     * Sets all pedestrians to their origin location
     */
    public void resetPedestrianLocation() {
        //clears the pedestrian arraylist
        pedestrians = new ArrayList<>();
        //loop through every cloned pedestrian
        for (IntelligentPedestrian p: pedestriansClone) {
            //set the right parameters for every pedestrian
            IntelligentPedestrian toAdd = new IntelligentPedestrian(p.originLocation);
            toAdd.setPreferredSpace(p.preferredSpace);
            toAdd.setRadius(p.getRadius());
            toAdd.setSpeed(p.getSpeed());
            toAdd.hasReachedTarget = false;

            //add the reseted pedestrian to the list
            pedestrians.add(toAdd);
        }
        //clone the new pedestrian list
        createPedestrianClone();
        change();
    }

    /**
     * Shortcut for the internal method "getColosionPedestrian".
     */
    public ArrayList<AbstractPedestrian> getColosionPedestrian(Point check, AbstractPedestrian whoAmI) {
        return getColosionPedestrian(check, whoAmI, false);
    }

    /**
     * Updates a target for every selected pedestrian
     *
     * @param convertedMousePosition the location of the target
     * @return true if at least one pedestrian has changed its target, otherwise
     * false
     */
    public boolean setGoalForSelectedPedestrians(Point convertedMousePosition) {
        boolean result = false;
        for (Wall w: getWalls()) {
            //which wall should be the nre target?
            if (w.doesTouch(convertedMousePosition)) {
                boolean hasChanged = false;

                //update target for every single pedestrian
                for (AbstractPedestrian a: getPedestrians()) {
                    if (a.isSelected()) {
                        hasChanged = true;
                        a.setTarget(w);
                        result = true;
                    }
                }

                //no pedestrian was selected. Update the goal for every pedestrian
                if (!hasChanged) {
                    for (AbstractPedestrian a: getPedestrians()) {
                        hasChanged = true;
                        a.setTarget(w);
                        result = true;
                    }
                }
                break;
            }
        }
        //update the fastest path of the pedestrians
        generateAllFastestPath();
        //notify the observers
        change();
        return result;
    }

    /**
     * Returns a HashMap with all the points that have a direct connection to
     * the pedestrian's target. The HashMap also contains the Point on the
     * target that can be reached directly.
     */
    public HashMap<Point, Point> getPointsWithDirectTargetConnection(AbstractPedestrian pedestrian) {
        //create caching-Parameters
        String method = "getPointsWithDirectTargetConnection";
        String unique = "" + pedestrian.getTarget() + pedestrian.getRadius();

        //get caching-result
        Object r = Cacher.get(method, unique);

        //is there a result?
        if (r != null) {
            //return the result
            return (HashMap<Point, Point>) r;
        }

        //create a new result
        HashMap<Point, Point> returnMap = new HashMap<>();
        //Loop through every Point
        for (Point p: getAllEdges(pedestrian)) {

            //has this point a direct goal Connection
            Point nearestGoalPoint = getDirectConnectionToGoal(p, pedestrian);
            if (nearestGoalPoint != null) {
                returnMap.put(p, nearestGoalPoint);
            }
        }

        //add all the collected points to the HashMap
        Point nearestGoalPoint = getDirectConnectionToGoal(pedestrian.getCurrentLocation(), pedestrian);
        if (nearestGoalPoint != null) {
            returnMap.put(pedestrian.getCurrentLocation(), nearestGoalPoint);

        }

        //Store the result
        return (HashMap<Point, Point>) Cacher.store(method, unique, returnMap);

    }

    /**
     * Returns the point on the pedestrian's target that can be reached directly
     * from a given point
     */
    public Point getDirectConnectionToGoal(Point fromPoint, AbstractPedestrian pedestrian) {
        Point nearestGoalPoint = null;
        if (pedestrian.getTarget() == null) {
            return null;
        }
        //convert the target into a list of lines
        ArrayList<Line2D.Double> lines = pedestrian.getTarget().getLines();
        for (Line2D.Double l: lines) { //loop through every line
            Point p1 = new Point();
            p1.setLocation(l.getP1());
            Point p2 = new Point();
            p2.setLocation(l.getP2());
            //get the minimal distance to the line
            Point minimalDistancePoint = NearestPointInLine.minimalDistancePoint(fromPoint, p1, p2);
            if ((nearestGoalPoint == null
                    || minimalDistancePoint.distance(fromPoint) < nearestGoalPoint.distance(fromPoint))
                    && isVisible(fromPoint, minimalDistancePoint, pedestrian, pedestrian.getTarget())) {
                //a new minimal distance was found
                nearestGoalPoint = minimalDistancePoint;
            }
        }
        return nearestGoalPoint;
    }

    /*Setter and Getter*/
    public Point getMousePosition() {
        return mousePosition;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public synchronized ArrayList<Wall> getWalls() {
        return walls;
    }

    public synchronized ArrayList<IntelligentPedestrian> getPedestrians() {
        return pedestrians;
    }

    public ArrayList<Point> getWallPoints() {
        return wallPoints;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public Polygon getSelection() {
        return selection;
    }

    public void setTemporaryBorder(Rectangle temporaryBoarder) {
        this.temporaryBoarder = temporaryBoarder;
        change();
    }

    public Rectangle getTemporaryBoarder() {
        return temporaryBoarder;
    }

    /*Private Methods*/

    /**
     * Calculates the angle between three Points
     */
    private double angleBetween(Point center, Point current, Point previous) {
        //See also: //http://stackoverflow.com/questions/7066792/angle-between-3-points-signed-bad-results
        return Math.toDegrees(Math.atan2(current.x - center.x, current.y - center.y)
                - Math.atan2(previous.x - center.x, previous.y - center.y));
    }

    /**
     * normalizes a given number
     */
    private int getDirection(int number) {
        //'normalizes' a number
        return Integer.compare(0, number);
    }

    /**
     * Returns if there are selected pedestrians on the map
     */
    private boolean hasSelectedPedestrians() {
        for (AbstractPedestrian p: pedestrians) { //loop through every pedestrian
            if (p.isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if a pedestrian would touch a wall on a given location
     *
     * @param check  the locatino where the pedestrian whould stand
     * @param whoAmI the pedestrian that should be checked
     * @return true if a wall would be intersected, otherwise false
     */
    private boolean doesPedestrianTouchWall(Point check, AbstractPedestrian whoAmI) {
        AbstractPedestrian clone = whoAmI.cloneThis();

        clone.internalSetCurrentLocation(check);
        //does pedestrian touch wall?
        for (Wall wall: walls) {
            if ((wall.doesTouch(clone.getMiddlePoint()))
                    || (wall.intersectsLines(clone))) {
                //pedestrian touches wall
                return false;
            }
        }
        return true;
    }

    /**
     * Returs if the map contains currently selected walls
     */
    private boolean hasSelectedWalls() {
        for (Wall w: walls) {
            if (w.isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if a pedestrian can walk directly from Point1 to Point2 without
     * touching a wall.
     *
     * @return
     */
    private boolean isVisible(Point point1, Point point2, AbstractPedestrian pedestrian, Wall ignoreWall) {
        if (point2 == null || point1 == null) {
            System.err.println("A point equals null"); //calculation not possible
            return false;
        }
        //convert the two points to a line
        Line2D.Double l1 = new Line2D.Double(point1, point2);
        for (Wall w: getWalls()) {
            if (w.equals(ignoreWall)) { //should this wall be ignored?
                continue;
            }
            for (Line2D.Double line: w.getLines()) { //does the line intersect the fall?
                if (lineIntersects(line, l1)) {
                    return false;
                }
            }

            //if the distance from this line segment and the point is smaller
            //than the radius of the pedestrian, the path ist not possible
            for (Point p2: w.getAllPoints()) {
                if (l1.ptSegDist(p2) < pedestrian.getRadius() - 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /*Checks if a line intersects another line*/
    private boolean lineIntersects(Line2D.Double from, Line2D.Double to) {
        if (from.intersectsLine(to)) {

            if (from.getP1().equals(to.getP1())) {
                //a point of one line is a part of another line -> lines do not intersect
                return false;
            }

            if (from.getP1().equals(to.getP2())) {
                //a point of one line is a part of another line -> lines do not intersect
                return false;
            }

            if (from.getP2().equals(to.getP1())) {
                //a point of one line is a part of another line -> lines do not intersect
                return false;
            }

            if (from.getP2().equals(to.getP2())) {
                //a point of one line is a part of another line -> lines do not intersect
                return false;
            }

            //check if the lines do intersect
            if (Line2D.ptSegDist(from.getX1(), from.getY1(), from.getX2(), from.getY2(), to.getX1(), to.getY1()) == 0) {
                return false;
            }

            return !(Line2D.ptSegDist(from.getX1(), from.getY1(), from.getX2(), from.getY2(), to.getX2(), to.getY2()) == 0);
        }
        return false;
    }

    /**
     * Clones the list of the pedestrians
     */
    private void createPedestrianClone() {
        pedestriansClone = new ArrayList<>(pedestrians);
    }

    /**
     * Returns a list with pedestrians that are too near to a pedestrian on a
     * given location
     *
     * @param check          the dummy-location of the pedestrian
     * @param whoAmI         the pedestrian itself
     * @param ignorePriority returns all pedestrians that are too near even if
     *                       the current pedestrians has priority
     * @return
     */
    private ArrayList<AbstractPedestrian> getColosionPedestrian(Point check, AbstractPedestrian whoAmI, boolean ignorePriority) {

        ArrayList<AbstractPedestrian> returnList = new ArrayList<>();

        //clone the pedestrian
        AbstractPedestrian clone = whoAmI.cloneThis();
        clone.setCurrentLocationNotInternal(check);
        for (AbstractPedestrian a: pedestrians) {
            if (a.equals(whoAmI)) {
                continue;
            }
            //check if the pedestrian is too near
            if ((clone.isToNearToPedestrian(a)) //decide if a pedestrian has priority over the other pedestrian
                    && (ignorePriority
                    || !whoAmI.behaviour.hasPriorityTo(a))
                    && !a.hasReachedTarget) {
                returnList.add(a);

            }
        }
        return returnList;
    }

    /**
     * deselects all walls and pedestrians
     */
    void deselectAll() {
        //deselect all pedestrians
        for (AbstractPedestrian a: pedestrians) {
            a.setSelected(false);
        }

        //deselect all walls
        for (Wall a: walls) {
            a.setSelected(false);
        }
    }

    /**
     * Clears the cache for calculations
     */
    private void clearWallCache() {
        Cacher.clear();
    }

    /**
     * checks if all pedestrians have reached their target
     *
     * @return true if all pedestrians have reached their target, otherwise
     * false
     */
    boolean allPedestriansHaveReachedTarget() {
        for (IntelligentPedestrian a: pedestrians) { //for every pedestrian...
            if (!a.hasReachedTarget) {
                return false;
            }
        }
        return true;
    }
}
