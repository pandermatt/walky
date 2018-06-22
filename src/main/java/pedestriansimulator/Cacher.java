package main.java.pedestriansimulator;

import java.util.HashMap;

/**
 * The Cacher is used to store results of calculations. By storing the results, 
 * the computer has to calculate less and the application becomes faster.
 * 
 * @author Pascal Andermatt, Jan Huber
 */
public class Cacher {
    
    //Cacher is a singletone class
    private static Cacher instance = null;
    
    //contains all the stores results
    HashMap<String, HashMap<Object, Object>> entries;

    /**
     * Creates a new Cacher
     */
    protected Cacher() {
        entries = new HashMap<>();
    }

    /**
     * Returns the single Instance of the Chacher
     * @return 
     */
    private static Cacher getInstance() {
        if (instance == null) {
            instance = new Cacher();
        }
        return instance;
    }

    /**
     * Returns a stored result for a given uniqueInput. 
     */
    public static Object get(String method, Object uniqueInput) {
        //Load method
        HashMap<Object, Object> methods = getInstance().entries.get(method);
        if (methods == null) {
            return null;
        }
        //load correct entry
        return methods.get(uniqueInput);
    }

    /**
     * Dismisses all stored calculations
     */
    public static void clear() {
        getInstance().entries = new HashMap<>();
    }

    /**
     * Stores a result for a given unique input
     */
    public static Object store(String method, Object uniqueInput, Object toStore, boolean shouldClone) {
        HashMap<Object, Object> methods = getInstance().entries.get(method); //does the method already exist?
        if (methods == null) { 
            //create new method
            HashMap<Object, Object> toAdd = new HashMap<>();
            toAdd.put(uniqueInput, toStore); //store unique input and result
            methods = toAdd;
            getInstance().entries.put(method, methods); //store new method
        } else {
            //sore unique input and result
            methods.put(uniqueInput, toStore);
        }


        return toStore;

    }
}
