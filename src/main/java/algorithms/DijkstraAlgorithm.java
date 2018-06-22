package main.java.algorithms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The {@code DijkstraAlgorithm} is used to find the fastest possible path
 * between two Points in a graph.
 *
 * This class was built with the help of StackOverflow.
 *
 * @author Luke (from Stackoverflow), Pascal Andermatt, Jan Huber
 * @see
 * <a>http://stackoverflow.com/questions/17480022/java-find-shortest-path-between-2-points-in-a-distance-weighted-map</a>
 * <a>https://de.wikipedia.org/wiki/Dijkstra-Algorithmus</a>
 */
public class DijkstraAlgorithm implements Serializable {

    /**
     * Enters the starting-point to calculate the fastest path from
     *
     * @param startVertex the point to start with
     */
    public void setStartPoint(Node startVertex) {
        //convert the vertex to a list
        PriorityQueue<Node> vertexList = buildVertexList(startVertex);
        while (!vertexList.isEmpty()) {
            Node currentVertex = vertexList.poll();
            //adds all neighbours to the list
            fixNeighbours(currentVertex);
            addAllEdges(currentVertex, vertexList);
        }
    }

    /**
     * Calculates the fastest path from the startPoint to the endPoint
     *
     * @param targetVertex the last point of the path
     * @return a list of {@code Vertex} representing the fastest path
     */
    public List<Node> createShortestPathToTarget(Node targetVertex) {
        List<Node> shortestPath = new ArrayList<Node>();
        for (Node currentVertex = targetVertex; currentVertex != null; currentVertex = currentVertex.getPrevious()) {
            shortestPath.add(currentVertex);
        }
        //nearest Point should be first
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    private PriorityQueue<Node> buildVertexList(Node startVertex) {
        //convert a single vertex to a list
        startVertex.minimalDistance = 0.;
        PriorityQueue<Node> vertexList = new PriorityQueue<Node>();
        vertexList.add(startVertex);
        return vertexList;
    }

    private void fixNeighbours(Node mainVertex) {
        //fix null-pointer exception
        if (mainVertex.neighboursIsNull()) {
            mainVertex.setNeighbours(new Edge[0]);
        }
    }

    private void addAllEdges(Node currentVertex, PriorityQueue<Node> vertexList) {
        //for every neighbour-edge
        currentVertex.addAllTargetToList(vertexList);

    }

}
