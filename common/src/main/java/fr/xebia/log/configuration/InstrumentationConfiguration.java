package fr.xebia.log.configuration;

/**
 * Interface for querying configuration
 */
public interface InstrumentationConfiguration {

    public ClassPattern getClassToInstrument();

}
