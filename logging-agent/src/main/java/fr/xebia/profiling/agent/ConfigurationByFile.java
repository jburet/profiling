package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;
import fr.xebia.log.configuration.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Load configuration with a properties file.
 * File path is configured in constructor
 */
public class ConfigurationByFile implements InstrumentationConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationByFile.class);

    private Properties configProperties = new Properties();

    private final String confFilePath;

    private RegExpClassPattern regExpClassPattern;

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
        List<String> classToInstrument = PropertiesUtils.convertPropertiesToList("log.package", configProperties);
        regExpClassPattern = new RegExpClassPattern(classToInstrument);
    }

    @Override
    public RegExpClassPattern getClassToInstrument() {
        return regExpClassPattern;
    }
}
