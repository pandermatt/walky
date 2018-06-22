package main.java.pedestrians;

/**
 * Each step a pedestrian takes produces a StepResult. A StepResult contains the
 * information if the fastestPath should be recalculated.
 *
 * @author Pascal Andermatt, Jan Huber
 */
public class StepResult {
    public final boolean recalculatePath; //true, if the fastest path should be recalculated
    public final double stepsize; //length of the step

    /**
     * Creates a new StepResult.
     */
    public StepResult(boolean recalculateMap, double stepsize) {
        this.recalculatePath = recalculateMap;
        this.stepsize = stepsize;
    }


}
