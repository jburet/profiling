package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.ClassPattern;
import fr.xebia.log.configuration.InstrumentationConfiguration;

import javax.security.auth.login.Configuration;

public class ConfigurationByServer implements InstrumentationConfiguration {
    @Override
    public ClassPattern getClassToInstrument() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
