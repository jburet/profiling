package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.ClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;

public class ConfigurationByProperties implements InstrumentationConfiguration {

    @Override
    public ClassPattern getClassToInstrument() {
        return new ClassPattern();
    }
}
