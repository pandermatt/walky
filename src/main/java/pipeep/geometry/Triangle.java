package main.java.pipeep.geometry;

/*
 * This class is part of the pipeep.geometry library and was not written by us
 */

/**
 * A three-sided, three-vertexed polygon. Has some more features than Polygon,
 * simply because there are more assumptions that can be made about triangles.
 * <p\>
 * <p>
 * Contains some ported code donated ad hoc (public domain) by asarkar of
 * #xkcd-cs on irc.foonetic.net
 */
public class Triangle extends BasePolygon {

    // lazily evaluated variables
    private double area = Double.NaN;
    private boolean isCCW;
    private boolean isCCWInit = false; // aids lazy evaluation

    public Triangle(Node NodeA, Node NodeB, Node NodeC) {
        super(NodeA, NodeB, NodeC);
    }

    private Node getNodeA() {
        return getNodes()[0];
    }

    private Node getNodeB() {
        return getNodes()[1];
    }

    private Node getNodeC() {
        return getNodes()[2];
    }

    private void getArea() {
        if (Double.isNaN(area)) {
            Node p = getNodeA();
            Node q = getNodeB();
            Node r = getNodeC();
            area = .5 * (-q.getX() * p.getY() + r.getX() * p.getY() + p.getX() *
                    q.getY() - r.getX() * q.getY() - p.getX() * r.getY() +
                    q.getX() * r.getY());
            if (area < 0) {
                area *= -1;
                isCCW = false;
            } else {
                isCCW = true;
            }
            isCCWInit = true;
        }
    }

    public boolean isCCW() {
        if (!isCCWInit) {
            getArea();
        }
        return isCCW;
    }

    @Override
    public boolean doesIntersectLine(Line l) {
        for (Line i: getLines()) {
            if (i.doesIntersect(l)) {
                return false;
            }
        }
        return true;
    }
}
