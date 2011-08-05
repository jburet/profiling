package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.DebugInstrumentationConfiguration;
import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.profiling.configuration.ClassInstrumentationConfiguration;
import fr.xebia.profiling.interceptor.Interceptor;
import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;
import fr.xebia.profiling.module.log.slf4j.Slf4jPerClassLogger;
import org.objectweb.asm.commons.StaticInitMerger;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

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
        // Use system properties for locate configuration file.
        // By default use ./logging-agent.properties
        String confFilePath = "logging-agent.properties";
        if (System.getenv().containsKey(Environnement.CONFIGURATION_FILE)) {
            confFilePath = System.getenv(Environnement.CONFIGURATION_FILE);
        }

        List<MethodExecutedCallInterceptor> interceptorList = new ArrayList<MethodExecutedCallInterceptor>();
        interceptorList.add(new Slf4jPerClassLogger());
        InstrumentationConfiguration configuration = new ConfigurationByFile(confFilePath);
        DebugInstrumentationConfiguration debugConfiguration = new ConfigurationByFile(confFilePath);
        new InstrumentationManager(inst, configuration, debugConfiguration, null, interceptorList, null);
        
    }

    interface Environnement {
        public static final String CONFIGURATION_FILE = "LoggingAgent.configuration.path";
    }

}
