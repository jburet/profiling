package fr.xebia.log.configuration;

import fr.xebia.log.configuration.RegExpClassPattern;

public interface DebugInstrumentationConfiguration {

    public RegExpClassPattern getInstrumentedClassToSave();

    public String saveLocation();

}
