package main.java.media;

//This class was created for another project and used here

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code ThemeManager}-class manages the loading of the external-ressources
 * which have to be included in the Game. The class can load images and plays
 * sounds in the background. It differs from different 'Themes' - Depending on
 * which is the actual {@code Theme}, different ressources are loaded.
 */
public class ThemeManager extends Observable implements Serializable, Cloneable {

    private int acctualThemeIndex;  //which is the index of the actual Theme?
    private final ArrayList<Theme> themes;    //contains all the Themes

    private final ArrayList<BackgroundMusicThread> streams; //Contains an arrayList with backgroundmusic streams
    private transient HashMap<String, Image> loadedImages; //Caches images which are already stored
    private boolean musicIsPlaying = false; //is actual Music Playing?
    private int currentMusicTrack; //which is the actual track number?

    /**
     * Constructs a ThemeManage
     */
    public ThemeManager() {
        themes = new ArrayList<>();
        addTheme("Orginal", "", "Pushy", new Color(34, 49, 63), 7, 20, 5);

        acctualThemeIndex = 0;
        streams = new ArrayList<>();
        loadedImages = new HashMap<>();
        musicIsPlaying = false;
        currentMusicTrack = 1;

        //notify oberservers
        change();
    }

    /**
     * @param comboText          Text showed in the ComboBox Selection
     * @param suffix             The suffix for the Files (_kbw => picture_kbw) to load the different Themes
     * @param titel              The Titel for the Startscreen
     * @param backgroundColor    The Background Color of the GUI
     * @param amountOfBackground The Amount of Background Songs for the selected Theme
     * @param amountOfWonSounds  The Amount of Songs, when you win a game, for the selected Theme
     * @param amountOfMoveSounds The Amount of Songs, for the Move of the Charakter, for the selected Theme
     */
    private void addTheme(String comboText, String suffix, String titel, Color backgroundColor, int amountOfBackground, int amountOfWonSounds, int amountOfMoveSounds) {
        themes.add(new Theme(comboText, suffix, titel, backgroundColor, amountOfBackground, amountOfWonSounds, amountOfMoveSounds));
        acctualThemeIndex = themes.size() - 1;
        change();
    }

    /**
     * @return Returns the Theme which is selected - ActualTheme
     */
    private Theme getActualTheme() {
        return themes.get(acctualThemeIndex);
    }

    /**
     * Returns the Graphic for the Theme
     *
     * @param name     The name of the Graphic
     * @param filetype The filetyp of the Graphic
     * @return The image with the name and the filetyp
     */
    public Image getGraphic(String name, String filetype) {
        //loadedImages = cache
        if (loadedImages == null) {
            loadedImages = new HashMap<>();
        }

        //for access to the ArrayList
        if (acctualThemeIndex < 0) {
            acctualThemeIndex = 0;
        }

        //generate the Path
        String path = name + themes.get(acctualThemeIndex).getSuffix();
        Image img = loadedImages.get(path);
        if (img != null) {
            return img;
        }

        String orginalPath = "/images/" + path + "." + filetype;

        //Read the iamge
        try {
            URL url = getClass().getResource(orginalPath);
            img = null;
            img = ImageIO.read(url);
            if (img == null) {
                throw new IOException();
            }
        } catch (Exception ex) {
            //There was not Image with this theme.

            //Is there an Image for the orginalTheme?
            try {
                img = ImageIO.read(getClass().getResource("/images/" + name + "." + filetype));
            } catch (IOException ex1) {
                //no Image was found
                System.err.println("Image was not found");
                Logger.getLogger(ThemeManager.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

        //cache the image that was found
        loadedImages.put(path, img);
        return img;
    }

    /**
     * Returns true or false for the Sound
     *
     * @param name     The name of the Song
     * @param filetype The filetyp of the Song
     * @return false, when Sound is unable to play - true, when Sound plays successful
     */
    @Deprecated
    public boolean playSound(String name, String filetype) {


        //Get Path of sound
        try {

            String orginalPath = "/sounds/" + name + themes.get(acctualThemeIndex).getSuffix() + "." + filetype;
            InputStream in = (this.getClass().getResourceAsStream(orginalPath));
//            AudioStream as = new AudioStream(in);

            //Play sound
//            AudioPlayer.player.start(as);
        } catch (Exception ex) {

            //No Sound for Theme was found...
            //Is there a sound for the original Theme?
            String orginalPath = "/sounds/" + name + "." + filetype;
            InputStream in = (this.getClass().getResourceAsStream(orginalPath));
//            AudioStream as = null;
//            try {
//                as = new AudioStream(in);
//            } catch (IOException ex1) {
//                Logger.getLogger(ThemeManager.class.getName()).log(Level.SEVERE, null, ex1);
//                System.err.println("A requested was not played");
//                return false;
//            }
            //Play Orginal Sound
//            AudioPlayer.player.start(as);
            //Logger.getLogger(ThemeManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /**
     * Play a sound in the Background
     *
     * @param name     The name of the Background Song
     * @param filetype The filetyp of the Background Song
     */
    private void playBackgroundMusic(final String name, final String filetype) {


        musicIsPlaying = true;

        //Add new audioThread
        BackgroundMusicThread audioThread = new BackgroundMusicThread(name, filetype, getActualTheme().getSuffix());
        streams.add(audioThread);
        audioThread.start();
    }

    /**
     * Plays a selected background song
     *
     * @param nummer The nummer of the background song to play
     */
    private void playExactTrack(int nummer) {
        playBackgroundMusic("Start" + nummer, "wav");
        currentMusicTrack = nummer;
        change();
    }

    /**
     * Starts the background music
     */
    public void startBackgroundMusic() {
        playExactTrack(currentMusicTrack);
    }

    /**
     * Plays the next background song
     */
    public void playNextTrack() {
        stopBackgroundMusic();
        currentMusicTrack++;
        if (currentMusicTrack <= getActualTheme().getAmountOfBackgroundSounds()) {
            playBackgroundMusic("Start" + currentMusicTrack, "wav");
        } else {
            currentMusicTrack = 1;
            playBackgroundMusic("Start" + currentMusicTrack, "wav");
        }
        change();
    }

    /**
     * Plays the last background song
     */
    public void playLastTrack() {
        stopBackgroundMusic();
        currentMusicTrack--;
        if (currentMusicTrack > 0) {
            playBackgroundMusic("Start" + currentMusicTrack, "wav");
        } else {
            currentMusicTrack = 1;
            playBackgroundMusic("Start" + currentMusicTrack, "wav");
        }
        change();
    }

    /**
     * Stops the current Song
     */
    private void stopBackgroundMusic() {
        for (BackgroundMusicThread stream: streams) {
            stream.stopMusic();
        }
        musicIsPlaying = false;
        change();
    }

    /**
     * Notify Observers
     */
    private void change() {
        setChanged();
        notifyObservers();

    }

    /**
     * Plays a random background music track
     */
    private void playRandomBackgroundMusic() {

        if (!isMusicPlaying()) {
            int index = (int) (Math.random() * getActualTheme().getAmountOfBackgroundSounds() + 1);
            playExactTrack(index);
            currentMusicTrack = index;
        }
        change();

    }

    /**
     * Stop the music and start randomly again
     *
     * @deprecated
     */
    public void newBackgroundSong() {
        stopBackgroundMusic();
        playRandomBackgroundMusic();
    }

    /**
     * Play a random sound if the User finish a level
     */
    public void playRandomWonSound() {
        playSound("Pushy" + ((int) (Math.random() * getActualTheme().getAmountOfWonSounds()) + 1), "wav");
    }


    /**
     * @return get the acctualTheme by Index
     */
    public int getAcctualThemeIndex() {
        return acctualThemeIndex;
    }

    /**
     * @param acctualThemeIndex set the acctualTheme by Index
     */
    public void setAcctualThemeIndex(int acctualThemeIndex) {
        this.acctualThemeIndex = acctualThemeIndex;
        change();
    }

    /**
     * @return the themes
     */
    public ArrayList<Theme> getThemes() {
        return themes;
    }

    /**
     * @return if musicIsPlaying
     */
    private boolean isMusicPlaying() {
        return musicIsPlaying;
    }

    /**
     * @return the current Music Track
     */
    public int getCurrentMusicTrack() {
        return currentMusicTrack;
    }

    /**
     * @return get the Text of the Theme
     */
    public String getThemeText() {
        return themes.get(acctualThemeIndex).getTitelString();
    }

    /**
     * @param selected the Theme which maches to the name
     * @deprecated
     */
    public void setThemeByName(String selected) {
        for (int i = 0; i < themes.size(); i++) {
            if (themes.get(i).getName().equals(selected)) {
                acctualThemeIndex = i;
                change();
                return;
            }
        }

    }

    /**
     * @return get the color of the Theme background
     */
    public Color getBackgroundColor() {
        if ((acctualThemeIndex >= themes.size()) || (acctualThemeIndex < 0)) {
            return new Color(34, 49, 63);
        }
        return getActualTheme().getBackgroundColor();
    }

}
