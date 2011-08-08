package fr.xebia.profiling.common.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractConfigurationByFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfigurationByFile.class);

    protected Properties configProperties = new Properties();

    protected final String confFilePath;

    public AbstractConfigurationByFile(String filePath) {
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
        loadConfiguration();
    }

    protected abstract void loadConfiguration();

}
