package fr.xebia.profiling.agent;


import fr.xebia.profiling.module.profiling.sampling.SamplingHandler;
import fr.xebia.profiling.module.profiling.sampling.SamplingInfo;

import java.util.Map;
import java.util.TreeSet;

public class StdoutLogger implements SamplingHandler {

    @Override
    public void updateSampling(Map<String, Map<String, SamplingInfo>> oldMap) {

        TreeSet<SamplingInfoWithClassAndMethodName> orderedSampling = new TreeSet<SamplingInfoWithClassAndMethodName>();

        // Reorder all in threeset
        for (String className : oldMap.keySet()) {
            for (String methodName : oldMap.get(className).keySet()) {
                orderedSampling.add(new SamplingInfoWithClassAndMethodName(className + "." + methodName, oldMap.get(className).get(methodName)));
            }
        }

        // Print hashset
        for (SamplingInfoWithClassAndMethodName sampling : orderedSampling) {
            System.out.println(sampling.name + " ; " + sampling.call + " ; " + sampling.executionTimeInNano);
        }

    }

    class SamplingInfoWithClassAndMethodName implements Comparable<SamplingInfoWithClassAndMethodName> {
        public final long call;
        public final long executionTimeInNano;
        public final String name;

        SamplingInfoWithClassAndMethodName(String name, SamplingInfo samplingInfo) {
            this.name = name;
            this.call = samplingInfo.getCall();
            this.executionTimeInNano = samplingInfo.getExecutionTime();
        }

        @Override
        public int compareTo(SamplingInfoWithClassAndMethodName otherSampling) {
            // First by execution time
            if (this.executionTimeInNano == otherSampling.executionTimeInNano) {
                return this.name.compareTo(otherSampling.name);
            } else {
                return (int) (this.executionTimeInNano - otherSampling.executionTimeInNano);
            }
        }
    }
}
