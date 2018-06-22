package main.java.recording;

import java.util.ArrayList;

/**
 * This static class contains all possible resolutions for the Recording-Frame
 *
 * @author Pascal Andermatt, Jan Huber
 */
class Resolutions {
    public static ArrayList<Resolution> getResolutions() {
        //add all resolutions...
        ArrayList<Resolution> returnList = new ArrayList<>();
        //returnList.add(new Resolution("32K", 16384, 8640));
        //returnList.add(new Resolution("16K", 32768, 8640));
        returnList.add(new Resolution("8K", 8192, 4320));
        returnList.add(new Resolution("4K", 4096, 2160));
        returnList.add(new Resolution("Full HD", 1920, 1080));
        returnList.add(new Resolution("HD", 1280, 720));
        returnList.add(new Resolution("VGA", 800, 480));
        //returnList.add(new Resolution("Minecraft", 400, 240));
        return returnList;
    }
}
