package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;

public class ConfigurationByServer implements InstrumentationConfiguration {
    @Override
    public RegExpClassPattern getClassToInstrument() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
