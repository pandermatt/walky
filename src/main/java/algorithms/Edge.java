package main.java.algorithms;

import java.io.Serializable;

/**
 * An {@code Edge} represents a connection between two {@code Nodes} with a
 * given weight or 'distance'
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Edge implements Serializable, Cloneable {

    private final Node targetNode;
    private final double weight; //the weight is used by the DijkstraAlgorithm

    /**
     * Creates a new {@code Edge} with a given target and weight;
     *
     * @param targetNode
     * @param weight
     */
    public Edge(Node targetNode, double weight) {
        this.targetNode = targetNode;
        this.weight = weight;
    }

    /**
     * Returns a copy of this object with a new reference.
     *
     * @return the cloned object
     * @throws CloneNotSupportedException if the targetNode can not be cloned
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Object o = super.clone();
        return new Edge((Node) targetNode.clone(), weight);
    }

    /* Setter and Getter */

    public Node getTargetNode() {
        return targetNode;
    }

    public double getWeight() {
        return weight;
    }
}
