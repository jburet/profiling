package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.DebugInstrumentationConfiguration;
import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.profiling.common.agent.AbstractMainAgent;
import fr.xebia.profiling.common.agent.RestransformClassListener;
import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;
import fr.xebia.profiling.module.log.slf4j.Slf4jPerClassLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;

public class MainAgent extends AbstractMainAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainAgent.class);
    private static DebugInstrumentationConfiguration debugConfiguration;

    /**
     * Used when started with command line
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        MainAgent main = new MainAgent();
        main.prepareInstrumentation(inst);
    }

    /**
     * Used when started after JVM
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        MainAgent main = new MainAgent();
        main.prepareInstrumentation(inst);
        main.retransformAllClasses(inst, new RestransformClassListener() {

            private int nbClassToTranform;

            @Override
            public void beginRetransform(int length) {
                System.out.println("Begin reinstrument");
                nbClassToTranform = length;
            }

            @Override
            public void updateTransform(int i) {
                System.out.println("Reinstrument : " + i + "/" + nbClassToTranform);
            }

            @Override
            public void endRetransform() {
                System.out.println("End reinstrument");
            }
        });
    }

    protected void prepareInstrumentation(Instrumentation inst) {
        // Use system properties for locate configuration file.
        // By default use ./logging-agent.properties
        String confFilePath = "logging-agent.properties";
        if (System.getenv().containsKey(Environnement.CONFIGURATION_FILE)) {
            confFilePath = System.getenv(Environnement.CONFIGURATION_FILE);
        }
        List<MethodExecutedCallInterceptor> interceptorList = new ArrayList<MethodExecutedCallInterceptor>();
        interceptorList.add(new Slf4jPerClassLogger());
        configuration = new ConfigurationByFile(confFilePath);
        debugConfiguration = new ConfigurationByFile(confFilePath);
        new InstrumentationManager(inst, configuration, debugConfiguration, null, interceptorList, null);
    }
}
