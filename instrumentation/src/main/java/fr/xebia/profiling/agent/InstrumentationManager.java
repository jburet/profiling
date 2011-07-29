package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.profiling.interceptor.*;
import fr.xebia.profiling.module.log.slf4j.Slf4jPerClassLogger;
import fr.xebia.profiling.module.profiling.sampling.SamplingMethod;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class InstrumentationManager {

    private List<Transformer> transformers = new ArrayList<Transformer>();

    public InstrumentationManager(Instrumentation instrumentation, InstrumentationConfiguration configuration,
                                  List<ClassLoadingInterceptor> classLoadingInterceptors,
                                  List<MethodExecutedCallInterceptor> methodExecutedCallInterceptors,
                                  List<AdviceMethodCallInterceptor> adviceMethodCallInterceptors) {
        // Load ASM Transformer
        // Only one implementation...
        transformers.add(new InterceptorTransformer());
        instrumentation.addTransformer(new ASMTransformationClassFileTransfomer(configuration.getClassToInstrument(), transformers));

        // Register interceptor
        if (classLoadingInterceptors != null) {
            for (ClassLoadingInterceptor cli : classLoadingInterceptors)
                InterceptorRegistry.registerClassLoadingInterceptor(cli);
        }

        if (methodExecutedCallInterceptors != null) {
            for (MethodExecutedCallInterceptor meci : methodExecutedCallInterceptors)
                InterceptorRegistry.registerMethodInterceptor(meci);
        }

        if (adviceMethodCallInterceptors != null) {
            for (AdviceMethodCallInterceptor amci : adviceMethodCallInterceptors)
                InterceptorRegistry.registerMethodInterceptor(amci);
        }
    }
}
