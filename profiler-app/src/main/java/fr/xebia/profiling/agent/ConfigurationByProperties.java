package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;

public class ConfigurationByProperties implements InstrumentationConfiguration {
    @Override
    public RegExpClassPattern getClassToInstrument() {
        return new RegExpClassPattern(null);
    }
}
