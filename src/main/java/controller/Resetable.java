package main.java.controller;

/**
 * An Interface for all MouseListeners that should implement a reset-method;
 *
 * @author Pascal Andermatt, Jan Huber
 */
public interface Resetable {

    /**
     * This method gets called, when a MouseListener is no longer active
     */
    public void reset();
}
