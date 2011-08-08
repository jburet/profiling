package fr.xebia.profiling.common.agent;

import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.log.configuration.RegExpClassPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMainAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMainAgent.class);

    protected static InstrumentationConfiguration configuration;

    protected void retransformAllClasses(Instrumentation inst, RestransformClassListener listener) {
        Class[] classLoaded = inst.getAllLoadedClasses();

        // Get class to instrument pattern
        RegExpClassPattern pattern = configuration.getClassToInstrument();
        // Construct list with all class to retransform
        List<Class> classesToTransform = new ArrayList<Class>();
        for (Class c : classLoaded) {
            if (pattern.isClassMatch(c)) {
                classesToTransform.add(c);
            }
        }

        listener.beginRetransform(classesToTransform.size());
        int i = 0;
        for (Class c : classesToTransform) {

            try {
                if (i++ % (classesToTransform.size() / 100) == 0) {
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

    protected interface Environnement {
        public static final String CONFIGURATION_FILE = "LoggingAgent.configuration.path";
    }

}
