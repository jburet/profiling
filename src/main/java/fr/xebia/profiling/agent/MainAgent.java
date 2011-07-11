package fr.xebia.profiling.agent;

import fr.xebia.profiling.configuration.ClassInstrumentationConfiguration;
import fr.xebia.profiling.configuration.MockConfigurationLoader;

import java.lang.instrument.Instrumentation;

public class MainAgent {

    /**
     * Used when started with command line
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        prepareInstrumentation(inst);
    }

    /**
     * Used when started after JVM
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {

    }


    private static void prepareInstrumentation(Instrumentation inst) {
        ClassInstrumentationConfiguration classInstrumentationConfiguration = new ClassInstrumentationConfiguration(new MockConfigurationLoader());
        InstrumentationManager instrumentationManager = new InstrumentationManager(inst, classInstrumentationConfiguration.getPatternForLog());

    }


}
