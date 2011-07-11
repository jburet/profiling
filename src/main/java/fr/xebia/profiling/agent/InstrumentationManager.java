package fr.xebia.profiling.agent;

import fr.xebia.profiling.configuration.ClassPattern;
import fr.xebia.profiling.interceptor.InterceptorRegistry;
import fr.xebia.profiling.interceptor.InterceptorTransformer;
import fr.xebia.profiling.interceptor.Transformer;
import fr.xebia.profiling.module.log.slf4j.Slf4jPerClassLogger;
import fr.xebia.profiling.module.profiling.sampling.SamplingMethod;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class InstrumentationManager {

    private List<Transformer> transformers = new ArrayList<Transformer>();

    public InstrumentationManager(Instrumentation instrumentation, ClassPattern pattern) {
        transformers.add(new InterceptorTransformer());
        instrumentation.addTransformer(new ASMTransformationClassFileTransfomer(pattern, transformers));
        InterceptorRegistry.registerMethodInterceptor(new Slf4jPerClassLogger());
        // Sampling method
        SamplingMethod samplingMethod = new SamplingMethod();
        InterceptorRegistry.registerClassLoadingInterceptor(samplingMethod);
        InterceptorRegistry.registerMethodInterceptor(samplingMethod);
    }
}
