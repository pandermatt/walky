package main.java.algorithms;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import main.java.pedestriansimulator.Map;

/**
 * A {@code Node} is part of a Graph. In this application, a {@code Node}
 * represents a single Point on the screen
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Node implements Comparable<Node>, Serializable, Cloneable {

    private final Point pointRepresentation; //the Point which the node represents
    private Edge[] neighbours; //contains all the Nodes which are connected to this Node
    public double minimalDistance = Double.POSITIVE_INFINITY; //used by the DijkstraAlgorithm
    private Node previous; //used by the DijkstraAlgorithm - tells which node was visited before this node

    /**
     * Creates a new {@code Node} with a given Point.
     *
     * @param represents the Point which the Node should represent
     */
    public Node(Point represents) {
        pointRepresentation = represents;
    }

    /**
     * Returns if a Node has no neighbours
     *
     * @return true if the node has no neighbours, otherwise false
     */
    public boolean neighboursIsNull() {
        return neighbours == null;
    }

    /**
     * Compares two Vertex for List-Sorting
     *
     * @param other the node which is compared to this node
     * @return result of comparing the minimalDistance
     */
    @Override
    public int compareTo(Node other) {
        return Double.compare(minimalDistance, other.minimalDistance);
    }

    /**
     * Returns a String that represents this node. Only used for Debugging
     *
     * @return a String which describes that Node
     */
    @Override
    public String toString() {
        return "(" + pointRepresentation.getX() + "/" + pointRepresentation.getY() + ")";
    }

    /**
     * Clones this Node
     *
     * @return a new copy of this Node but with a different reference
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Adds this Node to a vertex list, used by the DijkstraAlgorithm
     *
     * @param currentEdge the edge which link to this node
     * @param vertexList teh list to which this node should be added
     */
    private void addTargetToList(Edge currentEdge, PriorityQueue<Node> vertexList) {
        Node currentTarget = currentEdge.getTargetNode();
        double distance = currentEdge.getWeight();

        //find the path with the minimal distance
        double distanceToVertex = minimalDistance + distance;
        boolean isNewMininalDistance
                = (currentTarget == null)
                ? false
                : distanceToVertex < currentTarget.minimalDistance;

        if (isNewMininalDistance) {
            //replace minimal distance
            vertexList.remove(currentTarget);
            vertexList.add(currentTarget); //add current vertex to list

            //update minimalDistance
            currentTarget.minimalDistance = distanceToVertex;
            currentTarget.previous = this;
        }
    }

    /**
     * Clones itself and adds a copy to a given HashMap
     *
     * @param newData the HashMap to which this Node should be added
     */
    public void addCopyOfYourself(HashMap<Point, Node> newData) {
        Node toAdd = new Node(new Point(pointRepresentation.x, pointRepresentation.y));
        if (newData.containsKey(pointRepresentation)) {
            return; //This node is already added
        } else {
            //add the node
            newData.put(new Point(pointRepresentation.x, pointRepresentation.y), toAdd);
        }
        if (neighboursIsNull()) {
            return; //there are no neighbours to add
        }
        for (Edge e : neighbours) {
            //also add all neighbours
            e.getTargetNode().addCopyOfYourself(newData);
        }

    }

    /**
     * This method is used to clone a Node. All Neighbours of this node are
     * copied from a given list and updated
     *
     * @param newData contains all the nodes which were already cloned
     * @param orginalNode this node should later replace the orginalNode
     */
    public void recursivelySetNeighbours(HashMap<Point, Node> newData, HashMap<Point, Node> orginalNode) {

        if (neighbours != null) {
            return; //this was already added, skip
        }

        neighbours = new Edge[0];
        ArrayList<Edge> neighboursArrayList = new ArrayList<>();

        //Create a clone of this Node:
        Node myOrginalEdition = orginalNode.get(pointRepresentation);

        if (myOrginalEdition.neighboursIsNull()) {
            return; //There are no neighbours to add, skip
        }

        //Clone all the neighbours
        for (Edge e : myOrginalEdition.neighbours) {
            Node cloneEdgeVertex = newData.get(e.getTargetNode().pointRepresentation);
            cloneEdgeVertex.recursivelySetNeighbours(newData, orginalNode);
            neighboursArrayList.add(new Edge(cloneEdgeVertex, e.getWeight()));
            neighbours = Map.arrayFromList(neighboursArrayList);
        }
    }

    /**
     * Adds the target-nodes of all neighbours to a given list
     *
     * @param vertexList the list where the target-nodes should be added.
     */
    void addAllTargetToList(PriorityQueue<Node> vertexList) {
        for (Edge currentEdge : neighbours) {
            addTargetToList(currentEdge, vertexList);
        }
    }

    /*Setter and Getter*/
    
    public Point getPoint() {
        return new Point(pointRepresentation.x, pointRepresentation.y);
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {

        this.previous = previous;
    }

    public void setNeighbours(Edge[] neighbours) {

        this.neighbours = neighbours;
    }

}
