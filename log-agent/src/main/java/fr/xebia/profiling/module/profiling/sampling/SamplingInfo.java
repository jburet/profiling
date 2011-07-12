package fr.xebia.profiling.module.profiling.sampling;

public class SamplingInfo {

    private int call = 0;
    private long executionTimeInNano = 0;

    public void updateCall(long executionTime) {
        this.call++;
        this.executionTimeInNano += executionTime;
    }

    public int getCall() {
        return call;
    }

    public long getExecutionTime() {
        return executionTimeInNano;
    }
}
