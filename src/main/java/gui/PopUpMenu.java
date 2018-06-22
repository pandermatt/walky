package main.java.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import main.java.pedestriansimulator.ApplicationSingletone;

/**
 * A PopUpMenu shows up when the user clicks the right mouse-button. The choice
 * is context-sensitive
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class PopUpMenu extends JPopupMenu {

    //all possible choices
    private JMenuItem hide;
    private JMenuItem resetZoom;
    private JMenuItem delete;
    private JMenu border;
    private JMenuItem borderbyDisplay;
    private JMenu maps;
    private JMenuItem station;
    private JMenuItem clear;

    /**
     * Creates a new PopUpMenu
     */
    public PopUpMenu() {

        final boolean toolboxVisible = ApplicationSingletone.getMainWindow().getToolboxPanel().isVisible();

        /* Add all the possible menu-items*/
        /* Add: Toolbox show/hide menu-item*/
        //String of "Toolbox Hide/Show" depends on if the toolbox is visible or not
        hide = new JMenuItem(toolboxVisible ? "Hide toolbox" : "Show toolbox");
        hide.addActionListener(new ActionListener() { //add the following action
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationSingletone.getMainWindow().getToolboxPanel().setVisible(!toolboxVisible);
                ApplicationSingletone.getMainWindow().updateCursor();
            }
        });
        add(hide);
        addSeparator();

        /* Add: reset zoom menu-item*/
        //reset zoom resets the scaling to 1
        resetZoom = new JMenuItem("Reset zoom");
        resetZoom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //this code runs if the menu is clicked
                ApplicationSingletone.getMainWindow().updateCursor();
                ApplicationSingletone.getMainWindow().getToolboxPanel().resetZoom();
            }
        });
        add(resetZoom);
        addSeparator();

        /* Add: new Target menu-item*/
        if (ApplicationSingletone.getCurrentMap().hasSelectedElements()) { //only show this menu if there are selected elements

            delete = new JMenuItem("Make to target for all");
            delete.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //if clicked, the target for all pedestrians is changed
                    ApplicationSingletone.getCurrentMap().markSelectedWallsAsTarget();
                }
            });
            add(delete);
            addSeparator();
        }

        /* Add: remove selected elements menu-item*/
        if (ApplicationSingletone.getCurrentMap().hasSelectedElements()) { //this menu is only shown if there are selected elements
            delete = new JMenuItem("Remove selected elements");
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //this action gets called when the menu is clicked
                    ApplicationSingletone.getCurrentMap().removeSelectedElements();
                }
            });
            add(delete);

            addSeparator();
        }

        

        /* Add: add map menu-item*/
        maps = new JMenu("Add map");

        //add all the maps that can be impoired
        station = new JMenuItem("Add station");
        station.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationSingletone.getCurrentMap().addStation();
                ApplicationSingletone.getMainWindow().updateCursor();
            }
        });
        maps.add(station);

        add(maps);

        addSeparator();

        /* Add: add clear window menu-item*/
        clear = new JMenuItem("Clear window");
        clear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //remove all elements
                ApplicationSingletone.getCurrentMap().clear();
                ApplicationSingletone.getMainWindow().updateCursor();
            }
        });
        add(clear);
    }
}
