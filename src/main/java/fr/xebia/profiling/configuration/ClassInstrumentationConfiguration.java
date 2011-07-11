package fr.xebia.profiling.configuration;

/**
 * Manage Class instrumentation conf
 */
public class ClassInstrumentationConfiguration {

    private final ConfigurationLoader configurationLoader;

    public ClassInstrumentationConfiguration(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    public ClassPattern getPatternForLog(){
        return configurationLoader.loadClassToInstrument();
    }

}
