package main.java.gui;

import java.io.InputStream;
import java.io.Serializable;

/**
 * The {@code SoundPlayer}-class can play short sounds in a seperate Thread
 */
@Deprecated
public class SoundPlayer implements Serializable, Cloneable {

    /**
     * Plays a sound from a static context
     *
     * @param name     the name of the sound (without ending) to play. (Note: the
     *                 sound must be inside the package 'sounds')
     * @param filetype the filetype of the sound that should play
     */
    public static void play(String name, String filetype) {
        SoundPlayer player = new SoundPlayer();
        player.playSound(name, filetype);
    }

    /**
     * plays a sound from the ressource folder
     *
     * @param name     the name of the sound to be played
     * @param filetype the filetype of the sound to be played
     * @return false, when sound was unable to play - true, when sound played
     * successful
     */
    private void playSound(String name, String filetype) {
        //get path of sound
        try {
            //create path
            String orginalPath = "/sounds/" + name + "." + filetype;
            InputStream in = (this.getClass().getResourceAsStream(orginalPath));
//            AudioStream as = new AudioStream(in);

            //Play sound
//            AudioPlayer.player.start(as);
        } catch (Exception ex) {
            System.err.println("the requested sound was not able to play");
        }
    }

}
