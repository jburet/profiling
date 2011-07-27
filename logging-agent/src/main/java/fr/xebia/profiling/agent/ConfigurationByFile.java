package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.ClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Load configuration with a properties file.
 * File path is configured in constructor
 */
public class ConfigurationByFile implements InstrumentationConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationByFile.class);

    private Properties configProperties= new Properties();

    private final String confFilePath;

    public ConfigurationByFile(String filePath) {
        this.confFilePath = filePath;
        loadProperties();
    }

    private void loadProperties() {

        // Load properties from file
        FileInputStream in = null;
        try {
            in = new FileInputStream("defaultProperties");
            configProperties.load(in);
        } catch (IOException e) {
            LOGGER.error("Cannot load properties file : {}", confFilePath);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error("Cannot close properties file stream : {}", confFilePath);
            }
        }

        // Create or update configuration
    }

    @Override
    public ClassPattern getClassToInstrument() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
