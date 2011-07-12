package fr.xebia.profiling.configuration;

/**
 * Load configuration
 */
public interface ConfigurationLoader {

    public ClassPattern loadClassToInstrument();
}
