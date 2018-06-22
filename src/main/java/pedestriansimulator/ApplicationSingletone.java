package main.java.pedestriansimulator;

import main.java.gui.PedestrianSettingsPanel;
import main.java.gui.Walky;
import java.io.Serializable;
import java.util.Observable;
import main.java.media.ThemeManager;

/**
 * The ApplicationSingleton manages all the classes where only one instance exists.
 * @author Pascal Andermatt, Jan Huber
 */
public class ApplicationSingletone extends Observable implements Serializable {

    //Singleton-classes
    private Map map;
    private Walky mainWindow;
    private ThemeManager themeManager;
    
    private PedestrianSettingsPanel panel;
    //Singletone-Object
    private static ApplicationSingletone instance;

    /**
     * Creates a new ApplicationSingletone
     */
    private ApplicationSingletone() {
        map = new Map();
        mainWindow = null;
        themeManager = new ThemeManager();
        panel = new PedestrianSettingsPanel();
        
    }

    /**
     * Returns the only instance of the singleton
     * @return 
     */
    private static ApplicationSingletone getInstance() {
        if (instance == null) {
            //build object
            instance = new ApplicationSingletone();
        }
        return instance;
    }

    /*Setter and Getter*/
    
    public static void setCurrentMap(Map currentMap) {
        getInstance().map = currentMap;
    }

    public static Map getCurrentMap() {
        return getInstance().map;
    }

    public static void setMainWindow(Walky mainWindow) {
        getInstance().mainWindow = mainWindow;
    }

    public static Walky getMainWindow() {
        return getInstance().mainWindow;
    }

    public static PedestrianSettingsPanel getPedestrianPanel() {
        return getInstance().panel;
    }

    public static void mapHasChanged() {
        getInstance().mainWindow.mapHasChanged();
    }

    public static ThemeManager getThemeManager() {
        return getInstance().themeManager;
    }

}
