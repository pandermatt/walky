package main.java.openstreetmapparser;

import java.util.Comparator;
import org.w3c.dom.Element;

/**
 * A NodeLocation represents a single Point in a xml-map.
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class NodeLocation {

    String id;
    Element node;

    /**
     * Creates a new NodeLocation with a given element
     */
    public NodeLocation(Element node) {
        this.node = node;
        this.id = node.getAttribute("id");
    }

    /**
     * Creates a new NodeLocation with a given id
     */
    public NodeLocation(String id) {
        this.id = id;
        this.node = null;
    }

    /**
     * Returns the comperator to compare two NodeLocations (used for sorting)
     */
    public static Comparator<NodeLocation> getComperator() {
        Comparator<NodeLocation> comparator = new Comparator<NodeLocation>() {
            public int compare(NodeLocation o1, NodeLocation o2) {
                //use id for comparation
                if (o1.getId().equals(o2.getId())) {
                    return 0;
                }
                return o1.getId().compareTo(o2.getId());
            }
        };
        return comparator;
    }

    /*Setter and Getter*/
    public Element getNode() {
        return node;
    }

    public String getId() {
        return id;
    }

}
