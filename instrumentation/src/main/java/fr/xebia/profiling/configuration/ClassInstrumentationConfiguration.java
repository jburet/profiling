package fr.xebia.profiling.configuration;

/**
 * Manage Class instrumentation conf
 */
public class ClassInstrumentationConfiguration {

    private final Configuration configuration;

    public ClassInstrumentationConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ClassPattern getPatternForLog(){
        return configuration.getClassToInstrument();
    }

}
