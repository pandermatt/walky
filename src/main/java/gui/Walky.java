package main.java.gui;

import main.java.controller.PedestrianController;
import main.java.mapElements.Wall;
import main.java.openstreetmapparser.OpenStreetMapConverter;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Walky extends javax.swing.JFrame implements Observer {

    private boolean isMac;
    //all possible cursors
    private final Cursor pedestrianCursor;
    private final Cursor wallCursor;
    private final Cursor freehandWallCursor;
    private final Cursor clearCursor;
    private final Cursor borderCursor;
    private final Cursor shiftCursor;
    private final Cursor markGoalCursor;
    private main.java.gui.ToolboxPanel toolboxPanel1;
    private main.java.gui.ZoomAndPanCanvas zoomAndPanCanvas1;

    /**
     * Creates new form Walky-Form
     */
    private Walky() {
        //Set Window Title
        super("Walky");

        //check if the current os is mac os x -> fullscreen can be enabled
        String lcOSName = System.getProperty("os.name").toLowerCase();
        isMac = lcOSName.startsWith("mac os x");

        //This codeblock is from the internet...
        try {
            //Set Look And Feel for System
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Walky.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Get the cursor-images from the harddrive
        //Load clear-cursor image
        clearCursor = getCursor("cursor/no.png");

        this.setCursor(clearCursor);

        //Load pedestrian-cursor image
        pedestrianCursor = getCursor("cursor/ped.png");

        //Load shift-cursor image
        shiftCursor = getCursor("cursor/shift.png");

        //Load wall-cursor image
        wallCursor = getCursor("cursor/square.png");

        //Load freehand-cursor image
        freehandWallCursor = getCursor("cursor/poly.png");

        //Load border-cursor image
        borderCursor = getCursor("cursor/wall.png");

        //Load mark-goal-cursor image
        markGoalCursor = getCursor("cursor/Dart.png");

        changeForMac();
        initComponents();

        //initialize components
        PedestrianController controller = new PedestrianController(toolboxPanel1);

        //set action listener
        zoomAndPanCanvas1.addMouseListener(controller);
        zoomAndPanCanvas1.addMouseMotionListener(controller);
        zoomAndPanCanvas1.addKeyListener(controller);
        toolboxPanel1.addKeyListener(controller);

        //request focus
        toolboxPanel1.setFocusable(true);
        toolboxPanel1.requestFocus();
        toolboxPanel1.requestFocusInWindow();

        //enable fullscreen-mode if device is a mac
        enableOSXFullscreen(this);
        setScreenLocation();

        //update ApplicationSingletone
        ApplicationSingletone.setMainWindow(this);

        //Load the window for this icon
        Image windowIcon;
        try {
//            get icon from path
            windowIcon = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/icon.png"));
            setIconImage(windowIcon);
        } catch (IOException ignored) {
        }

        //update observer and zoomlistener
        ApplicationSingletone.getCurrentMap().addObserver(this);
        ApplicationSingletone.getCurrentMap().zoomListener = zoomAndPanCanvas1.zoomAndPanListener;

        //reset everythin
        zoomAndPanCanvas1.repaint();
        zoomAndPanCanvas1.resetZoom();
        ApplicationSingletone.getCurrentMap().setMouse(new Point(0, 0));

    }

    private static void enableOSXFullscreen(Window window) {
        //this codeblock is from the internet. It enebles the fullscreenmode for the mac
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info: javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException ignored) {
        } catch (IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Walky.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            //Start the application
            new Walky().setVisible(true);
        });
    }

    /**
     * If this application runs on a mac, the cursor must by shifted by 22
     *
     * @return the correct shift for the system
     */
    private int getMouseShiftForSystem() {
        return isMac ? -22 : 0;
    }

    //Set the window in the center of the screen
    private void setScreenLocation() {
        // get screen dimensions
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        // set frame width, height and let platform pick screen location
        setSize((int) (screenWidth / 1.5), (int) (screenHeight / 1.5));
        Point middle = new Point(screenWidth / 2, screenHeight / 2);

        //set the location
        Point newLocation;
        int pointX = middle.x - (getWidth() / 2);
        int pointY = middle.y - (getHeight() / 2);
        newLocation = new Point(pointX, pointY);
        setLocation(newLocation);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        toolboxPanel1 = new ToolboxPanel();
        zoomAndPanCanvas1 = new ZoomAndPanCanvas();
        JMenuBar jMenuBar1 = new JMenuBar();
        // Variables declaration - do not modify//GEN-BEGIN:variables
        JMenu jMenu2 = new JMenu();
        JMenuItem saveMap = new JMenuItem();
        JMenuItem loadMap = new JMenuItem();
        JMenuItem removeSelectedElements = new JMenuItem();
        JMenu jMenu4 = new JMenu();
        JMenuItem loadStation = new JMenuItem();
        JMenuItem loadWinterthur = new JMenuItem();
        JMenuItem loadPfaeffikon = new JMenuItem();
        JMenuItem loadBachenbuelach = new JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        toolboxPanel1.setMinimumSize(new java.awt.Dimension(200, 100));
        toolboxPanel1.setPreferredSize(new java.awt.Dimension(200, 75));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(toolboxPanel1, gridBagConstraints);

        javax.swing.GroupLayout zoomAndPanCanvas1Layout = new javax.swing.GroupLayout(zoomAndPanCanvas1);
        zoomAndPanCanvas1.setLayout(zoomAndPanCanvas1Layout);
        zoomAndPanCanvas1Layout.setHorizontalGroup(
                zoomAndPanCanvas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 573, Short.MAX_VALUE)
        );
        zoomAndPanCanvas1Layout.setVerticalGroup(
                zoomAndPanCanvas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 403, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(zoomAndPanCanvas1, gridBagConstraints);

        jMenu2.setText("Edit");

        saveMap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMap.setText("Save map...");
        saveMap.addActionListener(evt -> saveMapActionPerformed());
        jMenu2.add(saveMap);

        loadMap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        loadMap.setText("Load map...");
        loadMap.addActionListener(evt -> loadMapActionPerformed());
        jMenu2.add(loadMap);

        removeSelectedElements.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE, 0));
        removeSelectedElements.setText("Remove selected elements");
        removeSelectedElements.addActionListener(evt -> removeSelectedElementsActionPerformed());
        jMenu2.add(removeSelectedElements);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Templates");

        loadStation.setText("Bahnhof...");
        loadStation.addActionListener(evt -> loadStationActionPerformed());
        jMenu4.add(loadStation);

        loadWinterthur.setText("Büelrain...");
        loadWinterthur.addActionListener(evt -> loadWinterthurActionPerformed());
        jMenu4.add(loadWinterthur);

        loadPfaeffikon.setText("Winterthur Ausschnitt 1...");
        loadPfaeffikon.addActionListener(evt -> loadPfaeffikonActionPerformed());
        jMenu4.add(loadPfaeffikon);

        loadBachenbuelach.setText("Winterthur Ausschnitt 2...");
        loadBachenbuelach.addActionListener(evt -> loadBachenbuelachActionPerformed());
        jMenu4.add(loadBachenbuelach);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveMapActionPerformed() {//GEN-FIRST:event_saveMapActionPerformed
        //the current map should be saved
        JFileChooser chooser = new JFileChooser();

        //get the location where the map should be saved
        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) { //only save if the user pressed "ok"
            File returnFile = chooser.getSelectedFile();

            if (!returnFile.getAbsolutePath().endsWith(".map")) { //add .map-Ending to the path
                returnFile = new File(returnFile.getAbsolutePath() + ".map");
            }

            try {
                //write map to harddisk
                FileOutputStream outputStream = new FileOutputStream(returnFile);
                ObjectOutput output = new ObjectOutputStream(outputStream);

                output.writeObject(ApplicationSingletone.getCurrentMap());
                //clean output
                output.flush();
                output.close();
                outputStream.close();
            } catch (Exception e) {
                System.err.println("Application was not able to write file");
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_saveMapActionPerformed

    private void loadMapActionPerformed() {//GEN-FIRST:event_loadMapActionPerformed
        //Import a map
        JFileChooser jFileChooser = new JFileChooser();
        //only .map-files can be imported
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Pedestrian Maps", "map"));

        //User can choose a map with the dialog
        int result = jFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) { //only import map if user clicks "ok"

            try {
                //import the map
                FileInputStream stream = new FileInputStream(jFileChooser.getSelectedFile());
                ObjectInput objectInput = new ObjectInputStream(stream);
                Map importedMap = (Map) objectInput.readObject();

                ApplicationSingletone.setCurrentMap(importedMap);

                //apply changes
                importedMap.setupAfterImport();
                importedMap.change();

            } catch (ClassNotFoundException | IOException ex) {
                System.err.println("Map was not successfully imported");
            }
        }
    }//GEN-LAST:event_loadMapActionPerformed

    private void loadStationActionPerformed() {//GEN-FIRST:event_loadStationActionPerformed
        //draw station on the map
        ApplicationSingletone.getCurrentMap().addStation();

    }//GEN-LAST:event_loadStationActionPerformed

    private void loadWinterthurActionPerformed() {//GEN-FIRST:event_loadWinterthurActionPerformed
        //import the choosed map
        importMap("map.xml");
    }//GEN-LAST:event_loadWinterthurActionPerformed

    private void loadPfaeffikonActionPerformed() {//GEN-FIRST:event_loadPfaeffikonActionPerformed
        //import the choosed map
        importMap("map2.xml");
    }//GEN-LAST:event_loadPfaeffikonActionPerformed

    private void loadBachenbuelachActionPerformed() {//GEN-FIRST:event_loadBachenbuelachActionPerformed
        //import the choosed map
        importMap("map3.xml");
    }//GEN-LAST:event_loadBachenbuelachActionPerformed

    private void removeSelectedElementsActionPerformed() {//GEN-FIRST:event_removeSelectedElementsActionPerformed
        //remove selected elements from the map
        ApplicationSingletone.getCurrentMap().removeSelectedElements();
    }//GEN-LAST:event_removeSelectedElementsActionPerformed
    // End of variables declaration//GEN-END:variables

    public void updateCursor() {
        //chooses the right cursor for the selected tool
        if (toolboxPanel1.isPedestrianSelected()) {
            this.setCursor(pedestrianCursor);
        } else if (toolboxPanel1.isBorderSelected()) {
            this.setCursor(borderCursor);
        } else if (toolboxPanel1.isSquareSelected()) {
            this.setCursor(wallCursor);
        } else if (toolboxPanel1.isWallToolSelected()) {
            this.setCursor(freehandWallCursor);
        } else if (toolboxPanel1.isShiftSelected()) {
            this.setCursor(shiftCursor);
        } else if (toolboxPanel1.isMarkGoalSelected()) {
            this.setCursor(markGoalCursor);
        } else {
            this.setCursor(clearCursor);
        }
    }

    //Code from the internet
    private void changeForMac() {
        //check if the current os is max os x
        String lcOSName = System.getProperty("os.name").toLowerCase(); //code aus dem Internet
        isMac = lcOSName.startsWith("mac os x");
        System.out.println("Current OS: " + lcOSName);

        if (isMac) {
            //update menubar and fullscreenmode
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Pushy");

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
            }
            enableOSXFullscreen(this);
        }
    }

    /**
     * This method get called when a new map was imported. All MouseListener and Observers have to be reset.
     */
    public void mapHasChanged() {
        //Update controller
        PedestrianController controller = new PedestrianController(toolboxPanel1);

        //Update mouselistener
        zoomAndPanCanvas1.addMouseListener(controller);
        zoomAndPanCanvas1.addMouseMotionListener(controller);

        //update keyboard listener
        zoomAndPanCanvas1.addKeyListener(controller);

        //update canvas
        zoomAndPanCanvas1.mapHasChanged();

        //update observer
        ApplicationSingletone.getCurrentMap().addObserver(this);

        //reset zoom
        zoomAndPanCanvas1.repaint();
        zoomAndPanCanvas1.resetZoom();
        //reset mouselocation
        ApplicationSingletone.getCurrentMap().setMouse(new Point(0, 0));
    }

    /**
     * Converts a path to an image-file into a cursor
     *
     * @param path the path where the image is
     * @return converted image
     */
    private Cursor getCursor(String path) {

        //load image from the harddrive
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //update image and location
        return toolkit.createCustomCursor(toolkit.getImage(
                this.getClass().getClassLoader().getResource(path)), new Point(this.getX(),
                this.getY() + getMouseShiftForSystem()), "img");
    }


    private void importMap(final String path) {
        //clear old maps
        ApplicationSingletone.getCurrentMap().getWalls().clear();
        //import an xml-File that contains a map
        //map is imported in a seperate Thread
        Thread addMapThread = new Thread(() -> {
            try {
                //Convert map to walls
                ArrayList<Wall> walls = OpenStreetMapConverter.getWallsFromMap(path, zoomAndPanCanvas1.getSize());
                for (Wall p: walls) {
                    ApplicationSingletone.getCurrentMap().addWithoutCheck(p);
                }
            } catch (Exception ignored) {

            }
            //load trees
            ApplicationSingletone.getCurrentMap().addAllTrees(OpenStreetMapConverter.getTrees());
        });

        //start importing the map
        addMapThread.start();
        //notify user
        JOptionPane.showMessageDialog(null, path + " wird generiert.\nDies kann einige Minuten dauern.\nSobald der Prozess beendet ist, wird " + path + " auf die Karte geladen");
    }
    /*Setter and Getter*/

    @Override
    public void update(Observable o, Object arg) {
        //updateCursor();
    }

    public PedestrianComponent getZoomAndPanCanvas1() {
        return zoomAndPanCanvas1;
    }

    public PedestrianPanel getPedestrianPanel() {
        return zoomAndPanCanvas1.getPanel();
    }

    public ToolboxPanel getToolboxPanel() {
        return toolboxPanel1;
    }

}
