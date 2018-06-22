package main.java.gui;

import main.java.mapElements.Tree;
import main.java.mapElements.Wall;
import main.java.pedestrians.AbstractPedestrian;
import main.java.pedestrians.IntelligentPedestrian;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;
import main.java.polygonAlgorithms.PolygonHelper;
import main.java.recording.Resolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * The Panel is the view where the user can view the simulation and interact
 * with it.
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class PedestrianPanel extends JPanel implements Observer, Serializable {

    //static variables
    private final static int HOUSE_HEIGHT = 15;
    private final static Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    private final static Stroke fatdashed = new BasicStroke(6, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0, new float[]{21.0f, 9.0f, 3.0f, 9.0f}, 0);
    private static long lastPaintTime;
    //recording variables
    private boolean init = true;
    private boolean record;
    private int frameNumber = 0;
    private Resolution recordResolution;
    private String recordSavePath;
    //cached information
    private final ZoomMouseListener zoomAndPanListener;
    private Map map;

    /**
     * Creates a new PedestrianPanel
     */
    public PedestrianPanel() {
        //update zoom- and transformationlistener
        this.zoomAndPanListener = new ZoomMouseListener(this);
        this.addMouseListener(zoomAndPanListener);
        this.addMouseMotionListener(zoomAndPanListener);
        this.addMouseWheelListener(zoomAndPanListener);
        ApplicationSingletone.getCurrentMap().addObserver(this);
        //Reset recording variables
        frameNumber = 0;
        record = false;
        recordResolution = null;
        recordSavePath = null;
        lastPaintTime = 0;
        map = null;
    }

    /**
     * Draws an arrow onto the given graphics-object with the current
     * coordinates
     */
    private static void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        drawArrow(g1, x1, y1, x2, y2, 0.95);
    }

    /**
     * Is called from the method drawArrow. Does the calculation to draw an
     * arrow onto the Graphics-Object.
     */
    private static void drawArrow(Graphics g1, int x1, int y1, int x2, int y2, double length) {
        int arraySize = 4;

        Graphics2D g = (Graphics2D) g1.create(); //convert Graphics

        //calcluate arrow-coordinates
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        //calculate the length of the arrow
        len = (int) (len * length);
        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len, len - arraySize, len - arraySize, len},
                new int[]{0, -arraySize, arraySize, 0}, 4);
    }

    /**
     * Draws the current animation-image onto a Graphics2D-Object
     *
     * @param g2d
     */
    public void draw(Graphics2D g2d) {
        //Initialize settings for drawing
        GUISettings settings = ApplicationSingletone.getMainWindow().getToolboxPanel().getGuiSettings();
        boolean showDebugInformation = settings.showDebug;
        boolean animation = ApplicationSingletone.getMainWindow().getToolboxPanel().animationPlay;
        Stroke line = g2d.getStroke();
        map = ApplicationSingletone.getCurrentMap();

        //clear the screen for windows pc's
        clear(g2d);
        //must be called after clearing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //start drawing
        if (settings.showVisibleLines) {
            drawVisibleLines(g2d);
        }


        //draw all walls
        drawWalls(g2d, settings, line);

        //draw temporary walls
        if (!animation) {
            drawTemporaryBorder(g2d);
            if (ApplicationSingletone.getMainWindow().getToolboxPanel().isPedestrianSelected()) {
                drawTemporaryPedestrians(g2d, line);
            }
            drawTemporaryEdges(g2d);
        }

        //draw debug information
        if (showDebugInformation) {
            drawEdgeInformation(g2d);
            drawInformationString(g2d, animation);
        }

        //draw all the pedestrians
        g2d.setStroke(line);
        for (AbstractPedestrian p: map.getPedestrians()) {
            drawPedestrian(g2d, p, settings);

        }
    }

    /**
     * This method is called by the application to draw the simulation on the
     * screen
     *
     * @param g the Graphics-Object where the animation-image should be drawn on
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d;
        g2d = (Graphics2D) g.create(); //convert Graphics-Object
        draw(g2d); //draw animation
    }

    /**
     * If the map-model has changed, it is repainted on the screen
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    /**
     * After calling this method, all the painted images will be saved as an
     * image on the user's harddisk
     *
     * @param recordResolution the resolution of the images that should be saved
     * @param path             the path on the user's harddisk where the image should be
     *                         saved
     */
    public void startRecording(Resolution recordResolution, String path) {
        this.record = true;
        this.recordResolution = recordResolution;
        recordSavePath = path;
    }

    /**
     * After calling this method, no more images from the simulation will be
     * stored
     */
    public void pauseRecording() {
        this.record = false;
    }

    /**
     * Re-sets the observer for the map if the map has changed.
     */
    public void mapHasChanged() {
        ApplicationSingletone.getCurrentMap().addObserver(this);
    }

    /**
     * Tries to save an image onto the user's harddisk
     *
     * @param image the image to save
     * @param path  the path where the image should be saved
     * @throws IOException if the image can not be saved
     */
    private void tryToSaveImage(BufferedImage image, String path) throws IOException {
        File toSave = new File(path);
        toSave.createNewFile();
        ImageIO.write(image, "PNG", toSave);
    }

    /**
     * Saves an image at the current path-location
     *
     * @param image the image to save
     */
    private void saveImage(BufferedImage image) {
        try {
            //Try to save image at path 1
            tryToSaveImage(image, recordSavePath + "/frame" + frameNumber + ".jpg");
        } catch (Exception ex) {
            try {
                //Try to save image at path 2
                tryToSaveImage(image, recordSavePath + "frame" + frameNumber + ".jpg");
            } catch (Exception e) {
                //Image was not saved
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves a picture of the current simulation if recording is activated
     */
    public void recordIfNecessary() {
        if (record) {
            //create an image
            BufferedImage bi = new BufferedImage(recordResolution.getX(), recordResolution.getY(), BufferedImage.TYPE_INT_RGB); //Change into w and h
            Graphics2D g2d = bi.createGraphics();
            //convert the image to a graphics-object
            draw(g2d);
            saveImage(bi);
            frameNumber++;
            System.out.println("Frame: " + frameNumber);
        }
    }

    /**
     * Clears the Graphics2D-Object for windows pc's where the clearing is not
     * done automatically
     */
    private void clear(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY.darker().darker());
        g2d.fillRect(-100000, -100000, 200000, 200000);
    }

    /**
     * Draws a line between all the points with a direct connection. Used to
     * display a graph.
     */
    private void drawVisibleLines(Graphics2D g2d) {
        for (AbstractPedestrian ped: map.getPedestrians()) {
            for (Point to: map.getAllEdges(ped)) { //check all possible point-connections
                g2d.setColor(map.isVisible(to, ped.getCurrentLocation(), ped) ? Color.BLUE : new Color(0, 0, 0, 0));
                //Choose color: Blue for visible line, transparent for invisible line
                drawArrow(g2d, ped.getCurrentLocation().x, ped.getCurrentLocation().y, to.x, to.y);
            }
        }
    }

    /**
     * Draws a line from the pedestrian to the mouse-cursor in case the user is
     * setting a new target.
     */
    private void drawMarkTargetLine(AbstractPedestrian p, Graphics2D g2d) {
        if (ApplicationSingletone.getMainWindow().getToolboxPanel().isMarkGoalSelected()) {
            for (Wall w: map.getWalls()) {
                if (w.doesTouch(map.getMousePosition())) {
                    g2d.setColor(Color.YELLOW);
                }
            }
            //do the drawing
            g2d.drawLine(p.getCurrentLocation().x, p.getCurrentLocation().y, map.getMousePosition().x, map.getMousePosition().y);
        }
    }

    /**
     * Draws a pedestrian onto the screen.
     */
    private void drawPedestrian(AbstractPedestrian p, Graphics2D g2d) {
        g2d.setColor(p.getColor());
        //get the location where the drawing should start
        int x = p.getCurrentLocation().x - p.getRadius();
        int y = p.getCurrentLocation().y - p.getRadius();
        //width is the diameter for a pedestrian
        int width = 2 * p.getRadius();
        g2d.fillOval(x, y, width, width);
        g2d.setColor(Color.WHITE); //draw circle around pedestrian
        g2d.drawOval(x, y, width, width);
    }

    /**
     * Draws the preferred radius around a given pedestrian
     */
    private void drawPreferredRadius(AbstractPedestrian p, Graphics2D g2d) {
        g2d.setColor(Color.RED);
        //get coordinates
        int x = p.getCurrentLocation().x - p.getRadius() - p.preferredSpace;
        int y = p.getCurrentLocation().y - p.getRadius() - p.preferredSpace;
        //get witdh
        int width = 2 * (p.getRadius()) + 2 * p.preferredSpace;
        //do drawing
        g2d.drawOval(x, y, width, width);
    }

    /**
     * Draws the ideal path a pedestrian should take to reach the target as fast
     * as possible.
     */
    private void drawFastestPath(IntelligentPedestrian intP, Graphics2D g2d) {
        //loop through the path
        for (int i = 0; i < intP.getPath().size() - 1; i++) {
            Point current;
            if (i == 0) {
                current = intP.getCurrentLocation();
            } else {
                current = intP.getPath().get(i);
            }
            g2d.setColor(Color.ORANGE);
            try {
                //draw the next line
                Point next = intP.getPath().get(i + 1);
                drawArrow(g2d, current.x, current.y, next.x, next.y, 1);
            } catch (Exception e) {
                //There is not fastest path to draw
            }
        }
    }

    /**
     * Draws the location of every edge on the screen
     *
     * @param g2d
     */
    private void drawEdgeInformation(Graphics2D g2d) {
        IntelligentPedestrian currentMouse = new IntelligentPedestrian(map.getMousePosition());
        //load all the edges
        for (Point visible: map.getAllEdges(currentMouse)) {
            g2d.setColor(Color.WHITE);
            //draw the edge and string
            g2d.fillOval(visible.x - 5, visible.y - 5, 2 * 5, 2 * 5);
            g2d.drawString("X:" + visible.x + "/ Y:" + visible.y, visible.x, visible.y);
        }
    }

    /**
     * Draws the convex hull for every wall onto a given Graphics2D-Object
     */
    private void drawConvexHulls(Graphics2D g2d) {
        g2d.setStroke(dashed);
        for (Wall w: map.getWalls()) { //get all the walls
            g2d.setColor(w.getColor());
            //draw the convex hull
            g2d.drawPolygon(w.getOriginalConvexHull(new IntelligentPedestrian(null)));
        }
    }

    /**
     * Creates a 3D-Effect for a polygon depending on the current
     * camera-location
     */
    private void draw3DEffect(Graphics2D g2d, Wall wall, Polygon p) {
        //calculate where the camera is
        Point center = new Point(Math.round((float) g2d.getClipBounds().getCenterX()), (int) Math.round(g2d.getClipBounds().getCenterY()));
        //convert the polygon
        Polygon poly3d = apply3DEffect(p, center);

        //convert the two polygons into single lines
        ArrayList<Line2D.Double> lines1 = PolygonHelper.getLines(poly3d);
        ArrayList<Line2D.Double> lines2 = PolygonHelper.getLines(p);

        //connect the two polygons
        for (int i = 0; i < lines1.size(); i++) { //for each connection
            Line2D.Double line1 = lines1.get(i);
            Line2D.Double line2 = lines2.get(i);

            Polygon toDraw = new Polygon();

            Point2D p1 = line1.getP2();
            Point2D p2 = line1.getP1();
            Point2D p3 = line2.getP1();
            Point2D p4 = line2.getP2();

            //convert the lines into another polygon
            addRoundedPointToPolygon(toDraw, p1);
            addRoundedPointToPolygon(toDraw, p2);
            addRoundedPointToPolygon(toDraw, p3);
            addRoundedPointToPolygon(toDraw, p4);

            g2d.setColor(wall.getColor().darker().darker()); //creates a shadow-effect
            g2d.fillPolygon(toDraw); //draw the wall

        }
        g2d.setColor(wall.getColor());
        g2d.fill(poly3d); //Paint 3D-Effect

    }

    /**
     * Draws a single polygon onto the Graphics2D-Object
     */
    private void drawSinglePolygon(Graphics2D g2d, Wall wall, Polygon p, Stroke line) {
        g2d.setStroke(line);
        //get current color
        if (wall.isSelected()) {
            g2d.setColor(Color.BLUE);
        }
        g2d.setColor(wall.getColor());
        g2d.fill(p); //Draw wall

    }

    /**
     * Draw the image of a tree on the screen
     */
    private void drawTree(Graphics2D g2d, Tree tree) {
        //get the location of the screen
        int x = tree.getX() - tree.getRadius();
        int y = tree.getY() - tree.getRadius();
        int width = tree.getRadius() * 2;
        //draw the tree onto the screen
        g2d.drawImage(tree.getImage(), x, y, width, width, null);
    }

    /**
     * Draws a polygon onto the screen if the user is drawing a new wall
     */
    private void drawTemporaryEdges(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.setStroke(dashed);
        //draw the temporary wall
        for (Point p: map.getWallPoints()) {
            g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
        }
    }

    /**
     * Draws the temporary border onto the screen if the user is drawing a new
     * border
     */
    private void drawTemporaryBorder(Graphics2D g2d) {
        g2d.setStroke(dashed);
        g2d.setColor(Color.WHITE);
        g2d.drawPolygon(map.getSelection());
        //get the temporary rectangle
        Rectangle rectangle = map.getTemporaryBoarder();
        if (rectangle != null) {
            //get the coordinates where the rectangle should be drawn
            int x = Math.round((float) rectangle.getX());
            int y = Math.round((float) rectangle.getY());
            int width = Math.round((float) rectangle.getWidth());
            int height = Math.round((float) rectangle.getHeight());
            //draw the rectangle
            g2d.drawRect(x, y, width, height);
        }

        //Draw the current lines of the polygon
        for (int i = 1; i < map.getWallPoints().size(); i++) {
            //get the current preview line
            int x1 = map.getWallPoints().get(i - 1).x;
            int y1 = map.getWallPoints().get(i - 1).y;
            int x2 = map.getWallPoints().get(i).x;
            int y2 = map.getWallPoints().get(i).y;
            g2d.drawLine(x1, y1, x2, y2);
        }

        //connect the last point with the first point
        if (!map.getWallPoints().isEmpty()) {
            int x1 = map.getWallPoints().get(map.getWallPoints().size() - 1).x;
            int y1 = map.getWallPoints().get(map.getWallPoints().size() - 1).y;
            int x2 = map.getMousePosition().x;
            int y2 = map.getMousePosition().y;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Draws a preview where the pedestrians will be added if the user is
     * drawing new pedestrians
     */
    private void drawTemporaryPedestrians(Graphics2D g2d, Stroke line) {
        g2d.setStroke(line);

        //get the values of the temporary pedestrian
        int draw = ApplicationSingletone.getMainWindow().getToolboxPanel().pedDrawSize();
        int radius = ApplicationSingletone.getPedestrianPanel().getPedestrian().getRadius();
        int preferred = ApplicationSingletone.getPedestrianPanel().getPedestrian().preferredSpace;
        int diameter = 2 * radius;

        //loop through every single pedestrian if more than 1 pedestrian should be added
        for (int i = 0; i < draw; i++) {
            for (int j = 0; j < draw; j++) {
                int totalRadius = (radius + preferred);
                int multipliedRadius = totalRadius * draw;
                int doubleRadius = totalRadius * 2;
                //get the coordinates where the preview should be drawn
                int x = map.getMousePosition().x - (multipliedRadius) + i * doubleRadius;
                int y = map.getMousePosition().y - (multipliedRadius) + j * doubleRadius;

                Point checkPoint = new Point(x + radius + preferred, y + radius + preferred);
                //draw the preview if the pedestrian has a legal location
                if (map.isLegalPedestrianCoordinate(checkPoint, radius)) {
                    g2d.fillOval(x + preferred, y + preferred, diameter, diameter);
                }
            }
        }
    }

    /**
     * Draws a String with information about the current map onto the screen. Is
     * used for debugging.
     */
    private void drawInformationString(Graphics2D g2d, boolean animation) {
        if (animation) { //Debug-Information should not be visible during an animation
            return;
        }
        //calculate the current frames per second
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - lastPaintTime;
        lastPaintTime = currentTime;
        int fps = 0;
        if (difference != 0) {
            fps = (int) (1000 / difference);
        } else {
            fps = 10;
        }

        //Draw the frames per second onto the screen
        g2d.setColor(Color.WHITE);
        g2d.drawString("Pedestrians Alive: " + map.getPedestrians().size(), 20, 20);
        g2d.drawString("FPS: " + fps, 20, 40);
        if (map.getMousePosition() != null) {
            g2d.drawString("X:" + map.getMousePosition().x + "/ Y:" + map.getMousePosition().y, 20, 60);
        }

        //Display a warning when the frames per second are low
        ArrayList<String> infos = new ArrayList<>();
        if (fps < 10) {
            infos.add("Low Frame Rate (FPS)");
        }

        //draw all the other informations
        int i = 0;
        for (String info: infos) {
            g2d.drawString("[INFO] " + info, 20, 80 + 15 * i);
            i++;
        }
    }

    /**
     * Converts a polygon to apply a 3d-effect, depending on the current
     * camera-location;
     */
    private Polygon apply3DEffect(Polygon p, Point currentMouse) {

        //get the height of the 3D-effects
        int heightX = Math.round((float) (HOUSE_HEIGHT * zoomAndPanListener.getTransformation().getScaleX()));
        int heightY = Math.round((float) (HOUSE_HEIGHT * zoomAndPanListener.getTransformation().getScaleY()));

        //convert the x and y location
        ArrayList<Point> points = PolygonHelper.getPointsFromPolygon(p);
        for (Point point: points) {
            int x1 = point.x - currentMouse.x;
            int y1 = point.y - currentMouse.y;
            point.x += (x1 / (101 / heightX));
            point.y += (y1 / (101 / heightY));
        }

        //convert the coordinate-list back into a polygon
        Polygon newPoly = new Polygon();
        for (Point point: points) {
            newPoly.addPoint(point.x, point.y);
        }
        return newPoly;
    }

    /**
     * Draws a single pedestrian onto the screen
     */
    private void drawPedestrian(Graphics2D g2d, AbstractPedestrian p, GUISettings settings) {
        //get the current color of the pedestrian
        g2d.setColor(p.getColor());
        if (p.isSelected() || !map.hasSelectedElements()) {
            drawMarkTargetLine(p, g2d); //draws a line from the pedestrian to the target
        }
        drawPedestrian(p, g2d);
        if (settings.showPreferredRadius) {
            //draws the preferred radius of a pedestrian
            drawPreferredRadius(p, g2d);
        }

        IntelligentPedestrian intP = (IntelligentPedestrian) p;
        //draw fastest path
        if (settings.showLineToTarget) {
            drawFastestPath(intP, g2d);
        }
    }

    /**
     * Draws all the walls of a map onto a Graphics2D-Object
     */
    private void drawWalls(Graphics2D g2d, GUISettings settings, Stroke line) {
        try {
            if (settings.showConvexHull) {
                //draw all convex hulls
                drawConvexHulls(g2d);
            }
            for (Wall wall: map.getWalls()) {
                for (Polygon p: wall.getCurrentEdges()) {
                    //draw each wall-Polygon
                    drawSinglePolygon(g2d, wall, p, line);
                    if (settings.show3DEffect) { //apply 3D-Effect if necessary
                        draw3DEffect(g2d, wall, p);
                    }
                }
            }

            for (Tree tree: map.getTrees()) {
                //draw all trees
                drawTree(g2d, tree);
            }
        } catch (Exception e) {
            //Avoid current modification exception
        }
    }

    /**
     * Converts a Point2D into a normal Point and adds it to the Polygon
     */
    private void addRoundedPointToPolygon(Polygon toDraw, Point2D p1) {
        toDraw.addPoint(Math.round((float) p1.getX()), Math.round((float) p1.getY()));
    }
}
