package fr.xebia.profiling.agent;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class TestConfigurationByFile {

    @Test
    public void when_file_exist_and_contain_xebia_class_then_regexpClassPattern_must_match_xebia_class() {
        ConfigurationByFile configurationByFile = new ConfigurationByFile("logging-agent/src/test/resources/test-logging-agent.properties");
        assertTrue(configurationByFile.getClassToInstrument().isClassNameMatch("fr/xebia/log/test/Test"));
    }


    @Test
    public void when_file_exist_and_contain_xebia_class_then_regexpClassPattern_must_not_match_java_class() {
        ConfigurationByFile configurationByFile = new ConfigurationByFile("logging-agent/src/test/resources/test-logging-agent.properties");
        assertTrue(!configurationByFile.getClassToInstrument().isClassNameMatch("java/util/Test"));
    }

    @Test
    public void when_file_not_exist_then_regexpClassPattern_must_not_match_class() {
        ConfigurationByFile configurationByFile = new ConfigurationByFile("logging-agent/src/test/resources/no-test-logging-agent.properties");
        assertTrue(!configurationByFile.getClassToInstrument().isClassNameMatch("java/util/Test"));
        assertTrue(!configurationByFile.getClassToInstrument().isClassNameMatch("fr/xebia/log/test/Test"));
    }
}
