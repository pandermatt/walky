package main.java.pedestriansimulator;

import main.java.algorithms.Node;

import java.awt.*;
import java.util.HashMap;

/**
 * A Graph represents a map with nodes and connections.
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Graph {

    private HashMap<Point, Node> data; //contains all nodes

    /**
     * Builds a new Graph
     */
    public Graph() {
        data = new HashMap<>();
    }

    /**
     * Adds a new Node to the graph
     */
    public void add(Point p, Node v) {
        data.put(p, v);
    }

    /**
     * Returns the Node for a given Point
     */
    public Node get(Point p) {
        return data.get(p);
    }

    /**
     * Removes a Node on a given Point
     */
    public void remove(Point currentLocation) {
        data.remove(currentLocation);
    }

    /**
     * Recursively clones this Graph. Creates a new graph with equal content but
     * as a new reference.
     */
    @Override
    public Graph clone() {
        //Create an empty graph
        HashMap<Point, Node> newData = new HashMap<>();

        //add all keys
        Node toStart = data.get(data.keySet().toArray()[0]);

        //recursivley add data
        toStart.addCopyOfYourself(newData);

        //Add edges
        Node firstClone = newData.get(newData.keySet().toArray()[0]);

        //add neighbours
        firstClone.recursivelySetNeighbours(newData, this.data);

        //convert to graph
        Graph p = new Graph();
        p.data = newData;
        return p;
    }

}
