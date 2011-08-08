package fr.xebia.profiling.interceptor;

public interface ContextMananager {
    boolean match(String classname, String methodname, Object[] args);

    Long getContextIdentifier(String classname, String methodname, Object[] args);
}
