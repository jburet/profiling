package fr.xebia.profiling.module.profiling.sampling;

import java.util.Map;

public interface SamplingHandler {
    void updateSampling(Map<String, Map<String,SamplingInfo>> oldMap);
}
