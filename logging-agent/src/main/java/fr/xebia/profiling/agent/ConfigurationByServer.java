package fr.xebia.profiling.agent;

import fr.xebia.profiling.configuration.ClassPattern;
import fr.xebia.profiling.configuration.Configuration;

public class ConfigurationByServer implements Configuration {
    @Override
    public ClassPattern getClassToInstrument() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
