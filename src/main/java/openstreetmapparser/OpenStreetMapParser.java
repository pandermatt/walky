package main.java.openstreetmapparser;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import main.java.mapElements.Tree;

/**
 * This class is used to process xml-data from OpenStreetMap and extract
 * elements like trees, buildings etc.
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class OpenStreetMapParser {

    Document document;
    String filename;
    ArrayList<NodeLocation> nodeLocations;
    ArrayList<Tree> allTrees;

    /**
     * Creates a new parser with a given file.
     */
    public OpenStreetMapParser(String filename) throws URISyntaxException, IOException, Exception {
        this.filename = filename;
        System.out.println("XML wird eingelesen...");
        loadXML(); //converts the xml into many single nodes
        nodeLocations = new ArrayList<>();
        buildNodeLocations(); //creates all the buildings
        allTrees = loadTrees(); //creates all the trees
    }

    /**
     * Extract all the buildings from the .xml-Map
     *
     * @return a list of buildings
     */
    public ArrayList<Building> getBuildings() {
        System.out.println("XML wird verarbeitet");
        NodeList ways = document.getElementsByTagName("way"); //get all "ways"
        ArrayList<Building> buildings = new ArrayList<>();
        for (int i = 0; i < ways.getLength(); i++) { //for every "way"
            Element currentWay = (Element) ways.item(i);
            if (isBuilding(currentWay)) {
                NodeList nodes = currentWay.getElementsByTagName("nd"); //search nd's
                ArrayList<Location> locationPoints = new ArrayList<>();
                for (int j = 0; j < nodes.getLength(); j++) {
                    //extract NodeLocation
                    Element currentNode = (Element) nodes.item(j);
                    Location nodeLocation = getLocation(currentNode.getAttribute("ref"));
                    //save NodeLocation
                    locationPoints.add(nodeLocation);
                }
                //convert locations to building
                Building toAdd = new Building(locationPoints);
                //update the title of the building
                toAdd.setTitle(getTitleFromBuilding(currentWay));
                //add the current building to the list with all buildings
                buildings.add(toAdd);
            }
        }
        return buildings;
    }

    /**
     * Extract all the trees from the .xml-Map
     *
     * @return
     */
    public ArrayList<Tree> loadTrees() {
        System.out.println("Trees werden erfasst...");
        ArrayList<NodeLocation> trees = new ArrayList<>();
        //for each node....
        for (int i = 0; i < nodeLocations.size(); i++) {
            Element currentNode = nodeLocations.get(i).getNode();
            NodeList tags = currentNode.getElementsByTagName("tag");
            //for each tag ...
            for (int j = 0; j < tags.getLength(); j++) {
                Element currentTag = (Element) tags.item(j);
                //search attribute "k"
                if (currentTag.hasAttribute("k")) {
                    if (currentTag.getAttribute("k").equals("natural")) { //search trees
                        if (currentTag.getAttribute("v").equals("tree")) {
                            //tree found
                            trees.add(nodeLocations.get(i)); //add tree to list
                            break;
                        }
                    }
                }
            }
        }

        //now we have all trees
        ArrayList<Tree> mapTrees = new ArrayList<>();

        //update the location of the tree
        for (NodeLocation tree : trees) {
            Point2D.Double treeLocation = getLocation(tree.getId()).getProjectionPoint();
            mapTrees.add(new Tree(treeLocation.x, treeLocation.y));
        }
        return mapTrees;

    }

    /**
     * Returns alle the trees from the map with converted coordinates
     */
    public ArrayList<Tree> getTrees(Dimension screenDimension, Rectangle2D.Double view) {
        //trees are already loaded but with wrong coordinate
        for (Tree tree : allTrees) {
            tree.updateCoordinates(screenDimension, view); //update coordinate
        }
        return allTrees;
    }

    /*Private Methods*/
    /**
     * Returns the location of a node with a given id
     *
     * @param attribute the id of the node that should be searched
     * @return
     */
    private Location getLocation(String attribute) {
        //find the node with the correct id
        int index = Collections.binarySearch(nodeLocations, new NodeLocation(attribute), NodeLocation.getComperator());
        Element currentNode = nodeLocations.get(index).getNode();
        //read lat and lon
        String lat = currentNode.getAttribute("lat");
        String lon = currentNode.getAttribute("lon");
        //create a location
        return new Location(Double.parseDouble(lat), Double.parseDouble(lon));

    }

    /**
     * Returns the title-String of a building (as element)
     */
    private String getTitleFromBuilding(Element currentBuilding) {
        //search all tags
        NodeList tags = currentBuilding.getElementsByTagName("tag");
        for (int i = 0; i < tags.getLength(); i++) {
            Element currentTag = (Element) tags.item(i);
            //search tag with attribute "k" with value "name"
            if (currentTag.getAttribute("k").equals("name")) {
                String name = currentTag.getAttribute("v");
                //print name on console
                System.out.println("NAME: " + name);
                return name; //return the name
            }
        }
        return ""; //the name of the building was not found
    }

    /**
     * Reads in all nodes and saves them as "NodeLocation" into an arraylist
     */
    private void buildNodeLocations() {
        //Add all nodes
        System.out.println("Nodes werden eingelesen...");

        //get all elements with tag "node"
        NodeList nodes = document.getElementsByTagName("node");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element currentNodeLocation = (Element) nodes.item(i);
            //add the node to the list
            nodeLocations.add(new NodeLocation(currentNodeLocation));
        }

        //sort the list
        System.out.println("Nodes werden sortiert");
        Collections.sort(nodeLocations, NodeLocation.getComperator());
    }

    /**
     * returns if a given element has a given tag
     *
     * @return true, if the element contains the tag, otherwise false
     */
    private boolean hasTag(Element currentElement, String searchTag) {
        NodeList tags = currentElement.getElementsByTagName("tag");

        //loop through each tag of the element
        for (int j = 0; j < tags.getLength(); j++) {
            Element currentTag = (Element) tags.item(j);
            String tag = currentTag.getAttribute("k");
            //is it the correct tag?
            if (tag != null && (tag.equals(searchTag))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if a given element represents a building
     */
    private boolean isBuilding(Element currentElement) {
        return hasTag(currentElement, "building");

    }

    /**
     * Loads the xml-File into a document
     */
    private void loadXML() throws Exception {
        //read xml
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //get document from xml
        document = builder.parse(getRessource());
    }

    /**
     * Reads the xml into the application.
     */
    private File getRessource() throws URISyntaxException, IOException {
        String orginalPath = "/ressourcen/" + filename; //build filname
        return getFile(orginalPath); //open file
    }

    /**
     * Converts the xml-filename into a file.
     */
    private File getFile(String filename) throws URISyntaxException, IOException {
        URI uri = getClass().getResource(filename).toURI(); //build uri
        return new File(uri);
    }
}
