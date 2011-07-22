package fr.xebia.profiling.agent;

import fr.xebia.profiling.configuration.ClassPattern;
import fr.xebia.profiling.configuration.Configuration;

public class ConfigurationByProperties implements Configuration {
    @Override
    public ClassPattern getClassToInstrument() {
        return new ClassPattern();
    }
}
