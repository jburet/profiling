package fr.xebia.profiling.interceptor;


public interface MethodExecutedCallInterceptor {
    public void methodExecuted(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long executionTimeInNano);
}
