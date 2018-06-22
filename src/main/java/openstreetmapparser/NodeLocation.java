package main.java.openstreetmapparser;

import org.w3c.dom.Element;

import java.util.Comparator;

/**
 * A NodeLocation represents a single Point in a xml-map.
 *
 * @author Pascal Andermatt, Jan Huber
 */
class NodeLocation {

    private final String id;
    private final Element node;

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
        return (o1, o2) -> {
            //use id for comparation
            if (o1.getId().equals(o2.getId())) {
                return 0;
            }
            return o1.getId().compareTo(o2.getId());
        };
    }

    /*Setter and Getter*/
    public Element getNode() {
        return node;
    }

    public String getId() {
        return id;
    }

}
