package fr.xebia.profiling.interceptor;

public interface MethodCallInterceptor {

    public void enterMethod(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue);

    public void exitMethod(String className, String methodCall, long executionTimeInNano);

}
