package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.DebugInstrumentationConfiguration;
import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.log.configuration.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Load configuration with a properties file.
 * File path is configured in constructor
 */
public class ConfigurationByFile implements InstrumentationConfiguration, DebugInstrumentationConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationByFile.class);

    private Properties configProperties = new Properties();

    private final String confFilePath;

    private RegExpClassPattern regExpClassPattern;
    private RegExpClassPattern regExpClassToPattern;
    private String saveClassPath;

    public ConfigurationByFile(String filePath) {
        this.confFilePath = filePath;
        loadProperties();
    }

    private void loadProperties() {

        // Load properties from file
        FileInputStream in = null;
        try {
            in = new FileInputStream(confFilePath);
            configProperties.load(in);
        } catch (IOException e) {
            LOGGER.error("Cannot load properties file : {}", confFilePath);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LOGGER.error("Cannot close properties file stream : {}", confFilePath);
            }
        }

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
