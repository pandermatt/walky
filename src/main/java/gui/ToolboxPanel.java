package main.java.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import javax.accessibility.Accessible;
import javax.imageio.ImageIO;
import javax.swing.*;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.recording.RecordingFrame;

public class ToolboxPanel extends javax.swing.JPanel {

    public boolean animationPlay = false;
    JFrame addPedestrianFrame = new JFrame();
    GUISettings guiSettings;
    ArrayList<AbstractButton> buttons = new ArrayList<>();

    /**
     * Creates new form ToolboxPanel
     */
    public ToolboxPanel() {
        initComponents();
        //initialize all variables
        toggleSelectionTool.setSelected(true);
        addPedestrianFrame = new JFrame();
        guiSettings = new GUISettings();

        //remove focus
        setFocusable(false);

        //Load icon for every button
        setButtonIcon(toggleStart, "start.png");
        setButtonIcon(toggleClear, "clear.png");
        setButtonIcon(toggleRecord, "record.png");
        setButtonIcon(toggleReset, "reset_pedestrians.png");
        setButtonIcon(toggleFreehand, "addWall.png");
        setButtonIcon(toggleWall, "addWallSquare.png");
        setButtonIcon(togglePedestrian, "pedestrian.png");
        setButtonIcon(toggleBorder, "border.png");
        setButtonIcon(toggleSelectionTool, "select.png");
        setButtonIcon(toggleShift, "shift.png");
        setButtonIcon(toggleResetZoom, "reset_zoom.png");
        setButtonIcon(toggleSettings, "settings.png");
        setButtonIcon(toggleSetTarget, "goal.png");

        //add all buttons
        buttons.add(toggleStart);
        buttons.add(toggleClear);
        buttons.add(toggleRecord);
        buttons.add(toggleReset);
        buttons.add(toggleFreehand);
        buttons.add(toggleWall);
        buttons.add(togglePedestrian);
        buttons.add(toggleBorder);
        buttons.add(toggleSelectionTool);
        buttons.add(toggleShift);

        buttons.add(toggleResetZoom);
        buttons.add(toggleSetTarget);
        buttons.add(toggleSettings);
        
                setAllNotFocusable();

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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        toggleStart = new javax.swing.JToggleButton();
        toggleClear = new javax.swing.JButton();
        toggleRecord = new javax.swing.JButton();
        toggleReset = new javax.swing.JButton();
        toggleFreehand = new javax.swing.JToggleButton();
        toggleWall = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        togglePedestrian = new javax.swing.JToggleButton();
        toggleBorder = new javax.swing.JToggleButton();
        toggleSelectionTool = new javax.swing.JToggleButton();
        toggleShift = new javax.swing.JToggleButton();
        toggleResetZoom = new javax.swing.JButton();
        toggleSetTarget = new javax.swing.JToggleButton();
        toggleSettings = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridLayout(13, 1));

        toggleStart.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleStart.setText("Start");
        toggleStart.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        toggleStart.setIconTextGap(10);
        toggleStart.setPreferredSize(new java.awt.Dimension(142, 60));
        toggleStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleStartActionPerformed(evt);
            }
        });
        jPanel2.add(toggleStart);

        toggleClear.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleClear.setText("Clear");
        toggleClear.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleClear.setIconTextGap(10);
        toggleClear.setPreferredSize(new java.awt.Dimension(76, 60));
        toggleClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleClearActionPerformed(evt);
            }
        });
        jPanel2.add(toggleClear);

        toggleRecord.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleRecord.setText("Record");
        toggleRecord.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleRecord.setIconTextGap(10);
        toggleRecord.setPreferredSize(new java.awt.Dimension(138, 60));
        toggleRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleRecordActionPerformed(evt);
            }
        });
        jPanel2.add(toggleRecord);

        toggleReset.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleReset.setText("Reset");
        toggleReset.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleReset.setIconTextGap(10);
        toggleReset.setPreferredSize(new java.awt.Dimension(154, 60));
        toggleReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleResetActionPerformed(evt);
            }
        });
        jPanel2.add(toggleReset);

        buttonGroup1.add(toggleFreehand);
        toggleFreehand.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleFreehand.setText("Wall: Freehand");
        toggleFreehand.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleFreehand.setIconTextGap(10);
        toggleFreehand.setPreferredSize(new java.awt.Dimension(75, 60));
        toggleFreehand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleFreehandActionPerformed(evt);
            }
        });
        jPanel2.add(toggleFreehand);

        buttonGroup1.add(toggleWall);
        toggleWall.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleWall.setText("Rectangle Wall");
        toggleWall.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleWall.setIconTextGap(10);
        toggleWall.setPreferredSize(new java.awt.Dimension(116, 60));
        toggleWall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleWallActionPerformed(evt);
            }
        });
        jPanel2.add(toggleWall);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(togglePedestrian);
        togglePedestrian.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        togglePedestrian.setText("Pedestrian");
        togglePedestrian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        togglePedestrian.setIconTextGap(10);
        togglePedestrian.setPreferredSize(new java.awt.Dimension(109, 60));
        togglePedestrian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePedestrianActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(togglePedestrian, gridBagConstraints);

        jPanel2.add(jPanel1);

        buttonGroup1.add(toggleBorder);
        toggleBorder.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleBorder.setText("Border");
        toggleBorder.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleBorder.setIconTextGap(10);
        toggleBorder.setPreferredSize(new java.awt.Dimension(84, 60));
        toggleBorder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleBorderActionPerformed(evt);
            }
        });
        jPanel2.add(toggleBorder);

        buttonGroup1.add(toggleSelectionTool);
        toggleSelectionTool.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleSelectionTool.setText("Select");
        toggleSelectionTool.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleSelectionTool.setIconTextGap(10);
        toggleSelectionTool.setPreferredSize(new java.awt.Dimension(81, 60));
        toggleSelectionTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSelectionToolActionPerformed(evt);
            }
        });
        jPanel2.add(toggleSelectionTool);

        buttonGroup1.add(toggleShift);
        toggleShift.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleShift.setText("Shift");
        toggleShift.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleShift.setIconTextGap(10);
        toggleShift.setPreferredSize(new java.awt.Dimension(75, 60));
        toggleShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleShiftActionPerformed(evt);
            }
        });
        jPanel2.add(toggleShift);

        toggleResetZoom.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleResetZoom.setText("Reset Zoom");
        toggleResetZoom.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleResetZoom.setIconTextGap(10);
        toggleResetZoom.setPreferredSize(new java.awt.Dimension(118, 60));
        toggleResetZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleResetZoomActionPerformed(evt);
            }
        });
        jPanel2.add(toggleResetZoom);

        buttonGroup1.add(toggleSetTarget);
        toggleSetTarget.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleSetTarget.setSelected(true);
        toggleSetTarget.setText("Set Target");
        toggleSetTarget.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleSetTarget.setIconTextGap(10);
        toggleSetTarget.setPreferredSize(new java.awt.Dimension(95, 60));
        toggleSetTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSetTargetActionPerformed(evt);
            }
        });
        jPanel2.add(toggleSetTarget);

        toggleSettings.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        toggleSettings.setText("Graphic Settings");
        toggleSettings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        toggleSettings.setIconTextGap(10);
        toggleSettings.setPreferredSize(new java.awt.Dimension(118, 60));
        toggleSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSettingsActionPerformed(evt);
            }
        });
        jPanel2.add(toggleSettings);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void toggleSelectionToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleSelectionToolActionPerformed
        // Current Tool: Selection Tool
        selectionHasChanged();
    }//GEN-LAST:event_toggleSelectionToolActionPerformed

    private void toggleClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleClearActionPerformed
        // Clear all selected Elements
        if (ApplicationSingletone.getCurrentMap().hasSelectedElements()) {
            ApplicationSingletone.getCurrentMap().removeSelectedElements(); //remove only the selected elements
        } else {
            //remove all elements
            int result = JOptionPane.showConfirmDialog(this, "Do you really want to clear the map?", "Reset map", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                ApplicationSingletone.getCurrentMap().clear();

            }
        }

    }//GEN-LAST:event_toggleClearActionPerformed

    private void toggleFreehandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleFreehandActionPerformed
        // Current tool: Freehand wall tool
        selectionHasChanged();
    }//GEN-LAST:event_toggleFreehandActionPerformed

    private void toggleWallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleWallActionPerformed
        //Current tool: wall tool
        selectionHasChanged();
    }//GEN-LAST:event_toggleWallActionPerformed

    private void toggleBorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleBorderActionPerformed
        //Current tool: border tool
        selectionHasChanged();
    }//GEN-LAST:event_toggleBorderActionPerformed

    private void toggleRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleRecordActionPerformed
        // Start new Recording
        RecordingFrame recorder = new RecordingFrame(ApplicationSingletone.getMainWindow().getPedestrianPanel());
        //show new Recording-Frame
        recorder.setVisible(true);
    }//GEN-LAST:event_toggleRecordActionPerformed

    private void toggleResetZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleResetZoomActionPerformed
        //reset the current toom
        resetZoom();
    }//GEN-LAST:event_toggleResetZoomActionPerformed

    private void toggleShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleShiftActionPerformed
        // current tool: Shift tool
        selectionHasChanged();

    }//GEN-LAST:event_toggleShiftActionPerformed

    private void toggleStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleStartActionPerformed
        // Start or stop the Animation
        if (ApplicationSingletone.getCurrentMap().getPedestrians().isEmpty()) {
            //to start an animation, there must be at least one pedestrian
            JOptionPane.showMessageDialog(null, "Add at least 1 walky to the map", "no Walkies", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!animationPlay) {
            //start an animation
            ApplicationSingletone.getCurrentMap().startAnimation();

        } else {
            //stop a running animation
            ApplicationSingletone.getCurrentMap().stopAnimation();
        }
        animationPlay = !animationPlay;
        updateGUI();
    }//GEN-LAST:event_toggleStartActionPerformed

    private void toggleResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleResetActionPerformed
        //reset the location of all pedestrians
        ApplicationSingletone.getCurrentMap().resetPedestrianLocation();
    }//GEN-LAST:event_toggleResetActionPerformed

    private void toggleSetTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleSetTargetActionPerformed
        //current tool: set target tool
        ApplicationSingletone.getMainWindow().updateCursor();
    }//GEN-LAST:event_toggleSetTargetActionPerformed

    private void togglePedestrianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togglePedestrianActionPerformed
        //current tool: draw pedestrian tool
        selectionHasChanged();
        Dimension size = new Dimension(430, 260);

        //sets the fixed size for the "addPedestrianFrame"
        addPedestrianFrame.setSize(size);
        addPedestrianFrame.setMinimumSize(size);
        addPedestrianFrame.setMaximumSize(size);
        addPedestrianFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addPedestrianFrame.setContentPane(ApplicationSingletone.getPedestrianPanel());
        addPedestrianFrame.setVisible(true);
    }//GEN-LAST:event_togglePedestrianActionPerformed

    private void toggleSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleSettingsActionPerformed
        //Open settings panel

        Dimension size = new Dimension(200, 300);
        //sets the fixed size for the Frame with the GUI-Settings

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1, 1));
        frame.setSize(size);
        frame.setMinimumSize(size);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.add(guiSettings); //add panel to the frame
        frame.setVisible(true);
    }//GEN-LAST:event_toggleSettingsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToggleButton toggleBorder;
    private javax.swing.JButton toggleClear;
    private javax.swing.JToggleButton toggleFreehand;
    private javax.swing.JToggleButton togglePedestrian;
    private javax.swing.JButton toggleRecord;
    private javax.swing.JButton toggleReset;
    private javax.swing.JButton toggleResetZoom;
    private javax.swing.JToggleButton toggleSelectionTool;
    private javax.swing.JToggleButton toggleSetTarget;
    private javax.swing.JButton toggleSettings;
    private javax.swing.JToggleButton toggleShift;
    private javax.swing.JToggleButton toggleStart;
    private javax.swing.JToggleButton toggleWall;
    // End of variables declaration//GEN-END:variables

    /**
     * Resets the zoom-factor to 1 for the current map-component
     */
    void resetZoom() {
        ApplicationSingletone.getMainWindow().getZoomAndPanCanvas1().resetZoom();
        ApplicationSingletone.getCurrentMap().change();

    }

    public void updateGUI() {

        //update animation-Icon
        if (animationPlay) {
            toggleStart.setText("Stop Animation");
            setButtonIcon(toggleStart, "pause.png");
        } else {
            toggleStart.setText("Start Animation");
            setButtonIcon(toggleStart, "start.png");
        }

        //Refresh animation-cion
        toggleStart.revalidate();
        toggleStart.repaint();
        toggleStart.setVisible(false);
        toggleStart.setVisible(true);

        //activate or deactivate other buttons
        ApplicationSingletone.getCurrentMap().clearPoints();
        toggleBorder.setEnabled(!animationPlay);
        togglePedestrian.setEnabled(!animationPlay);
        toggleShift.setSelected(animationPlay);
        toggleWall.setEnabled(!animationPlay);
        toggleSelectionTool.setEnabled(!animationPlay);
        toggleFreehand.setEnabled(!animationPlay);
        toggleReset.setEnabled(!animationPlay);
        toggleRecord.setEnabled(!animationPlay);
        toggleSetTarget.setEnabled(!animationPlay);
        toggleClear.setEnabled(!animationPlay);

    }

    private void setAllNotFocusable() {
        //remove focus from all buttons

        for (AbstractButton button : buttons) {
            button.setFocusable(false);
        }

        jPanel2.setFocusable(false);

    }

    private void setButtonIcon(AbstractButton button, String icon) {

        //icon should be the image on the button
        button.setIcon(null); //remove current icon

        try {
            final BufferedImage master;

            //convert icon to url
            URL test = this.getClass().getResource("/buttonRessource/" + icon);

            //read image
            master = ImageIO.read(test);

            //icons should be resized if the button is resized
            button.addComponentListener(new ComponentAdapter() {

                //this codeblock is from the internet
                @Override
                public void componentResized(ComponentEvent e) {
                    AbstractButton btn = (AbstractButton) e.getComponent();
                    Dimension size = btn.getSize();
                    Insets insets = btn.getInsets();
                    size.width -= insets.left + insets.right;
                    size.height -= insets.top + insets.bottom;
                    if (size.width > size.height) {
                        size.width = -1;
                    } else {
                        size.height = -1;
                    }
                    Image scaled = master.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(scaled));
                }

            });

            //Update icon-dimension
            Dimension size = button.getSize();
            Insets insets = button.getInsets();
            size.width -= insets.left + insets.right;
            size.height -= insets.top + insets.bottom;
            if (size.width > size.height) {
                size.width = -1;
            } else {
                size.height = -1;
            }

            //Scale the icon to the correct size
            Image scaled = master.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);

            //add the icon for the button
            button.setIcon(new ImageIcon(scaled));

        } catch (Exception e) {
        }

    }

    /**
     * Clears all temporary points on the map
     */
    private void selectionHasChanged() {
        ApplicationSingletone.getMainWindow().updateCursor();
        ApplicationSingletone.getCurrentMap().clearPoints();
    }

    /*Setter and Getter*/
    /*The following methods tell, which tool is currently selected:*/
    public boolean isPedestrianSelected() {
        return togglePedestrian.isSelected();
    }

    public void setPedSelected() {
        togglePedestrian.setSelected(true);
    }

    public boolean isSquareSelected() {
        return toggleWall.isSelected();
    }

    public void setSquareSelected() {
        toggleWall.setSelected(true);
    }

    public boolean isBorderSelected() {
        return toggleBorder.isSelected();
    }

    public void setBorderSelected() {
        toggleBorder.setSelected(true);
    }

    public void setPolySelected() {
        toggleFreehand.setSelected(true);
    }

    public boolean isSelectionToolSelected() {
        return toggleSelectionTool.isSelected();
    }

    public void setSelectionTool() {
        toggleSelectionTool.setSelected(true);
    }

    public boolean isWallToolSelected() {
        return toggleFreehand.isSelected();
    }

    public int pedDrawSize() {
        return ApplicationSingletone.getPedestrianPanel().getPedestrianBrushSize();
    }

    public boolean isShiftSelected() {
        return toggleShift.isSelected();
    }

    public boolean isMarkGoalSelected() {
        return toggleSetTarget.isSelected();
    }

    public GUISettings getGuiSettings() {
        return guiSettings;
    }

}
