package fr.xebia.log.configuration;

/**
 *
 */
public class ClassPattern {

    public boolean isClassNameMatch(String classname){
        return classname.startsWith("fr/xebia/profiling/testclasses/SimpleClasses");
    }
}
