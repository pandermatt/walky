package main.java.recording;

import main.java.gui.PedestrianPanel;
import main.java.pedestriansimulator.ApplicationSingletone;
import main.java.pedestriansimulator.PedestrianAnimator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The RecordingFrame is a window which gives the user the possibility to record
 * an animation
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class RecordingFrame extends javax.swing.JFrame {

    private static final String ownResolution = "Own Resolution";
    private final PedestrianPanel pedestrianPanel; //this panel is displayed inside the frame
    private PedestrianAnimator animator; //animator to animate the pedestrians
    private String saveLocationPath; //where should the frames be stored?
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton chooseLocation;
    private javax.swing.JTextField location;
    private javax.swing.JComboBox resolutionBox;
    private javax.swing.JButton startButton;
    private javax.swing.JTextField xTextField;
    private javax.swing.JTextField yTextField;
    /**
     * Creates new form RecordingFrame
     */
    public RecordingFrame(PedestrianPanel panel) {
        pedestrianPanel = panel;
        initComponents();
        saveLocationPath = "";
        addResolutions();
        updateLocationTextField();
        cancelButton.setEnabled(false);
        updateStartButton();
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

        JLabel jLabel1 = new JLabel();
        JPanel jPanel1 = new JPanel();
        JLabel jLabel2 = new JLabel();
        resolutionBox = new javax.swing.JComboBox();
        JLabel jLabel5 = new JLabel();
        xTextField = new javax.swing.JTextField();
        chooseLocation = new javax.swing.JButton();
        location = new javax.swing.JTextField();
        JLabel jLabel3 = new JLabel();
        JLabel jLabel4 = new JLabel();
        yTextField = new javax.swing.JTextField();
        JPanel jPanel2 = new JPanel();
        cancelButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        JLabel jLabel6 = new JLabel();
        JLabel jLabel7 = new JLabel();
        JLabel jLabel8 = new JLabel();

        setMinimumSize(new java.awt.Dimension(615, 250));
        setPreferredSize(new java.awt.Dimension(615, 250));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel1.setText("Start new recording");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Save here:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 17);
        jPanel1.add(jLabel2, gridBagConstraints);

        resolutionBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"[FullHD] 1920 x 1080", "Item 2", "Item 3", "Item 4"}));
        resolutionBox.addActionListener(evt -> resolutionBoxActionPerformed());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(resolutionBox, gridBagConstraints);

        jLabel5.setText("Pixels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 17);
        jPanel1.add(jLabel5, gridBagConstraints);

        xTextField.setPreferredSize(new java.awt.Dimension(100, 28));
        xTextField.addActionListener(evt -> xTextFieldActionPerformed());
        xTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                xTextFieldKeyTyped();
            }

            public void keyPressed(java.awt.event.KeyEvent evt) {
                xTextFieldKeyPressed();
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                xTextFieldKeyReleased();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(xTextField, gridBagConstraints);

        chooseLocation.setText("Choose Location");
        chooseLocation.addActionListener(evt -> chooseLocationActionPerformed());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(chooseLocation, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(location, gridBagConstraints);

        jLabel3.setText("Resolution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 17);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setText("x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel1.add(jLabel4, gridBagConstraints);

        yTextField.setMinimumSize(new java.awt.Dimension(100, 28));
        yTextField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                yTextFieldInputMethodTextChanged();
            }

            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        yTextField.addActionListener(evt -> yTextFieldActionPerformed());
        yTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                yTextFieldKeyTyped();
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                yTextFieldKeyReleased();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(yTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 18, 0, 18);
        getContentPane().add(jPanel1, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(evt -> cancelButtonActionPerformed());

        startButton.setText("Start");
        startButton.addActionListener(evt -> startButtonActionPerformed());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 161, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(startButton)
                                        .addGap(0, 0, 0)
                                        .addComponent(cancelButton)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 29, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(startButton)
                                                .addComponent(cancelButton))
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 18);
        getContentPane().add(jPanel2, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 51, 102));
        jLabel6.setText("Bei der Aufnahme wird der gewählte Zoomfaktor nicht berücksichtigt.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 18);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 51, 102));
        jLabel7.setText("Vosicht: Die Aufnahme wird als Bild-Sequenz und nicht als Film-Datei gespeichert.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 18, 0, 18);
        getContentPane().add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 51, 102));
        jLabel8.setText("Bitte wählen Sie einen Ordner als Speicherort und nicht z.B. den Desktop");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 18);
        getContentPane().add(jLabel8, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resolutionBoxActionPerformed() {//GEN-FIRST:event_resolutionBoxActionPerformed

        //update GUI
        updateResolutionTextfields();
        updateStartButton();


    }//GEN-LAST:event_resolutionBoxActionPerformed

    private void chooseLocationActionPerformed() {//GEN-FIRST:event_chooseLocationActionPerformed
        //Create a new FileChooser
        JFileChooser fc = new JFileChooser();

        //Only directories should be choosen
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Any folder"; //desscripion
            }
        });

        //change settings for dialog
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setApproveButtonText("Select");
        int result = fc.showDialog(this, "Save images here");
        if (result != JFileChooser.CANCEL_OPTION) {
            //show dialog
            saveLocationPath = fc.getSelectedFile().getAbsolutePath();
            //update GUI
            updateLocationTextField();
        }

        updateStartButton();

    }//GEN-LAST:event_chooseLocationActionPerformed

    private void xTextFieldActionPerformed() {//GEN-FIRST:event_xTextFieldActionPerformed
        //updateGUI
        updateStartButton();

    }//GEN-LAST:event_xTextFieldActionPerformed

    private void yTextFieldActionPerformed() {//GEN-FIRST:event_yTextFieldActionPerformed
        //updateGUI
        updateStartButton();

    }//GEN-LAST:event_yTextFieldActionPerformed

    private void cancelButtonActionPerformed() {//GEN-FIRST:event_cancelButtonActionPerformed
        //re-enable GUI
        pedestrianPanel.pauseRecording();
        animator.stop();
        cancelButton.setEnabled(false);
        startButton.setEnabled(true);
        resolutionBox.setEnabled(true);
        chooseLocation.setEnabled(true);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void xTextFieldKeyPressed() {//GEN-FIRST:event_xTextFieldKeyPressed
        //update GUI
        updateStartButton();
    }//GEN-LAST:event_xTextFieldKeyPressed

    private void yTextFieldInputMethodTextChanged() {//GEN-FIRST:event_yTextFieldInputMethodTextChanged
        //update GUI
        updateStartButton();
    }//GEN-LAST:event_yTextFieldInputMethodTextChanged

    private void xTextFieldKeyTyped() {//GEN-FIRST:event_xTextFieldKeyTyped
        //update GUI
        updateStartButton();
    }//GEN-LAST:event_xTextFieldKeyTyped

    private void yTextFieldKeyTyped() {//GEN-FIRST:event_yTextFieldKeyTyped
        //update GUI
        updateStartButton();
    }//GEN-LAST:event_yTextFieldKeyTyped

    private void xTextFieldKeyReleased() {//GEN-FIRST:event_xTextFieldKeyReleased
        //update GUI
        updateStartButton();
    }//GEN-LAST:event_xTextFieldKeyReleased

    private void yTextFieldKeyReleased() {//GEN-FIRST:event_yTextFieldKeyReleased
        //update GUI
        updateStartButton();
    }//GEN-LAST:event_yTextFieldKeyReleased

    private void startButtonActionPerformed() {//GEN-FIRST:event_startButtonActionPerformed
        //Start new ewcording with given parameters
        pedestrianPanel.startRecording(new Resolution(null, Integer.parseInt(xTextField.getText()), Integer.parseInt(yTextField.getText())), saveLocationPath);

        animator = new PedestrianAnimator(ApplicationSingletone.getCurrentMap(), false);
        ApplicationSingletone.getCurrentMap().generateAllFastestPath();

        //start animation
        Thread t = new Thread(() -> {
            while (true) {
                //move all pedestrians
                animator.moveAllPedestrians();
                ApplicationSingletone.getCurrentMap().change();
            }
        });

        //start animation
        t.start();

        //disable GUI
        cancelButton.setEnabled(true);
        startButton.setEnabled(false);
        resolutionBox.setEnabled(false);
        chooseLocation.setEnabled(false);

    }//GEN-LAST:event_startButtonActionPerformed
    // End of variables declaration//GEN-END:variables

    private void addResolutions() {
        //reset default-items
        resolutionBox.removeAllItems();
        Dimension d = ApplicationSingletone.getMainWindow().getZoomAndPanCanvas1().getSize();
        //add item with current panel-size
        resolutionBox.addItem(new Resolution("Window Resolution", d.width, d.height));
        resolutionBox.addItem(ownResolution);
        //add all resolutions
        for (Resolution resolution: Resolutions.getResolutions()) {
            resolutionBox.addItem(resolution);
        }
        resolutionBox.setSelectedIndex(0);
        //update GUI
        updateResolutionTextfields();

    }

    private void updateResolutionTextfields() {
        //update the values of all textfields

        //enable or disable textfields
        if ((resolutionBox == null) || (resolutionBox.getSelectedItem() == null) || (resolutionBox.getSelectedItem().equals(ownResolution))) {
            xTextField.setEnabled(true);
            yTextField.setEnabled(true);
        } else {
            xTextField.setEnabled(false);
            yTextField.setEnabled(false);

            //get currnet values
            Resolution currentResolution = (Resolution) resolutionBox.getSelectedItem();

            //fill textfield with current Values
            xTextField.setText("" + currentResolution.getX());
            yTextField.setText("" + currentResolution.getY());
        }
    }

    private void updateLocationTextField() {
        //updates the value of the textfield "location"
        location.setEnabled(false);
        location.setText(saveLocationPath);
    }

    private void updateStartButton() {
        //enables or disables the start-button
        startButton.setEnabled(canStartRecording());
    }

    private boolean canStartRecording() {
        //returns if the recording can be started
        if (saveLocationPath == null || saveLocationPath.isEmpty()) {
            return false;
        }

        //get default values for x and y
        int x = 0;
        int y = 0;

        try {
            //convert textfield to integer
            x = Integer.parseInt(xTextField.getText());
            y = Integer.parseInt(yTextField.getText());
        } catch (Exception e) {
            return false;
        }

        //x and y must be > 0
        return (x > 0) && (y > 0);
    }
}
