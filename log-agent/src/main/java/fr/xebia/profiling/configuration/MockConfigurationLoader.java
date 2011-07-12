package fr.xebia.profiling.configuration;

public class MockConfigurationLoader implements ConfigurationLoader {
    @Override
    public ClassPattern loadClassToInstrument() {
        return new ClassPattern();
    }
}
