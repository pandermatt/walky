package main.java.media;

//This class was created for another project and used here

import java.awt.*;
import java.io.Serializable;

/**
 * The {@code Theme}-class represents a 'theme' for the
 * {@code ThemeManager}-class where the user can switch between different
 * themes.
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class Theme implements Serializable, Cloneable {

    private final String name;    //name of the theme
    private final String suffix;  //the suffix for the Ressource-Files
    private final String titelString; //A String which welcomes the User
    private final Color backgroundColor;  //A custom Background Theme for the theme
    private final int amountOfBackgroundSounds; //How many BackgroundSounds does this Theme contain?
    private final int amountOfWonSounds;  //How many Won-Sounds does thie Theme contain?
    private final int amountOfMoveSounds; //How many Move-Sounds does thie Theme contain?

    public Theme(String name, String suffix, String titelString, Color backgroundColor, int amountOfBackgroundSounds, int amountOfWonSounds, int amountOfMoveSounds) {
        this.name = name;
        this.suffix = suffix;
        this.titelString = titelString;
        this.backgroundColor = backgroundColor;
        this.amountOfBackgroundSounds = amountOfBackgroundSounds;
        this.amountOfWonSounds = amountOfWonSounds;
        this.amountOfMoveSounds = amountOfMoveSounds;
    }

    //Getter for fields

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @return the backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @return the amountOfBackgroundSounds
     */
    public int getAmountOfBackgroundSounds() {
        return amountOfBackgroundSounds;
    }

    /**
     * @return the amountOfWonSounds
     */
    public int getAmountOfWonSounds() {
        return amountOfWonSounds;
    }

    /**
     * @return the amountOfMoveSounds
     */
    public int getAmountOfMoveSounds() {
        return amountOfMoveSounds;
    }

    /**
     * @return the titelString
     */
    public String getTitelString() {
        return titelString;
    }

}
