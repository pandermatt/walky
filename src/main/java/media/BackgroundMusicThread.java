package main.java.media;

//This class was created for another project and used here

import javax.sound.sampled.*;
import java.io.Serializable;

/**
 * The {@code BackgroundMusicThread}-class can play Music in a seperate Thread.
 * It can be stoped without calling the deprecated method stop(). This class was developed originally for the project "Pushy"
 *
 * @author Pascal Andermatt, Jan Huber
 */
class BackgroundMusicThread extends Thread implements Serializable {

    private final String filename;
    private final String filetype;
    private final String suffix;
    private boolean shouldPlay = true;

    public BackgroundMusicThread(String filename, String filetype, String suffix) {
        super();
        this.filename = filename;
        this.filetype = filetype;
        this.suffix = suffix;
    }

    //Stop the Music
    public void stopMusic() {
        shouldPlay = false;
    }

    private boolean playFromPath(String path) {
        //A Part of this codeblock is from the internet

        try {
            //load Audiostream
            AudioInputStream audioInputStream = null;
            audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(path));

            AudioFormat audioFormat = audioInputStream.getFormat();
            SourceDataLine line = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();

            //Read data
            int nBytesRead = 0;
            byte[] abData = new byte[30000];

            //Play Sound
            while (nBytesRead != -1 && (shouldPlay)) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    int nBytesWritten = line.write(abData, 0, nBytesRead);
                }
            }
            line.drain();
            line.close();

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    @Override
    public void run() {
        while (true) {

            //Play Theme Sound
            String orginalPath = "/sounds/" + filename + suffix + "." + filetype;
            if (!playFromPath(orginalPath)) ;
            {
                orginalPath = "/sounds/" + filename + "." + filetype;
                //Play original Sound
                playFromPath(orginalPath);
            }

        }
    }

}
