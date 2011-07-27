package fr.xebia.profiling.configuration;

import fr.xebia.log.configuration.ClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;

/**
 * Manage Class instrumentation conf
 */
public class ClassInstrumentationConfiguration {

    private final InstrumentationConfiguration configuration;

    public ClassInstrumentationConfiguration(InstrumentationConfiguration configuration) {
        this.configuration = configuration;
    }


    public ClassPattern getPatternForLog(){
        return configuration.getClassToInstrument();
    }

}
