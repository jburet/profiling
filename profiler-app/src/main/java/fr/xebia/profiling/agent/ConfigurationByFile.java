package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.DebugInstrumentationConfiguration;
import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.log.configuration.PropertiesUtils;
import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.profiling.common.agent.AbstractConfigurationByFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Load configuration with a properties file.
 * File path is configured in constructor
 */
public class ConfigurationByFile extends AbstractConfigurationByFile implements InstrumentationConfiguration, DebugInstrumentationConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationByFile.class);

    private RegExpClassPattern regExpClassPattern;
    private RegExpClassPattern regExpClassToPattern;
    private String saveClassPath;

    public ConfigurationByFile(String filePath) {
        super(filePath);
    }

    protected void loadConfiguration() {
        // Create or update configuration
        // Logged class
        List<String> classToInstrument = PropertiesUtils.convertPropertiesToList("log.package", configProperties);
        regExpClassPattern = new RegExpClassPattern(classToInstrument);
        // Saved class
        List<String> classToSave = PropertiesUtils.convertPropertiesToList("debug.classes.save.package", configProperties);
        regExpClassToPattern = new RegExpClassPattern(classToSave);
        saveClassPath = (String) configProperties.get("debug.classes.save.path");
    }

    @Override
    public RegExpClassPattern getClassToInstrument() {
        return regExpClassPattern;
    }

    @Override
    public RegExpClassPattern getInstrumentedClassToSave() {
        return regExpClassToPattern;
    }

    @Override
    public String saveLocation() {
        return saveClassPath;
    }
}
