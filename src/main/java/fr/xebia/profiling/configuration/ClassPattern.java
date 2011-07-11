package fr.xebia.profiling.configuration;

/**
 *
 */
public class ClassPattern {

    public boolean isClassMatch(Class clazz){
        return clazz.getPackage().getName().startsWith("fr.xebia.profiling.testclasses.SimpleClasses");
    }

    public boolean isClassNameMatch(String classname){
        return classname.startsWith("fr/jbu/profiling/testclasses/SimpleClasses");
    }
}
