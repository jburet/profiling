package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;
import fr.xebia.profiling.module.log.slf4j.Slf4jPerClassLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;

public class MainAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainAgent.class);

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
        prepareInstrumentation(inst);
        retransformAllClasses(inst, new RestransformClassListener() {

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

    private static void retransformAllClasses(Instrumentation inst, RestransformClassListener listener) {
        Class[] classesToTransform = inst.getAllLoadedClasses();
        listener.beginRetransform(classesToTransform.length);
        int i = 0;
        for (Class c : classesToTransform) {
            try {
                if (i++ % (classesToTransform.length / 100) == 0) {
                    listener.updateTransform(i);
                }
                inst.retransformClasses(c);
            } catch (UnmodifiableClassException e) {
                LOGGER.warn("Cannot instrument class : {}", c.getName());
                LOGGER.debug("Instrument exception : ", e);
            }
        }
        listener.endRetransform();
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
        new InstrumentationManager(inst, configuration, null, interceptorList, null);

    }

    interface Environnement {
        public static final String CONFIGURATION_FILE = "LoggingAgent.configuration.path";
    }

}
