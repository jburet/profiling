package fr.xebia.profiling.interceptor;


public interface MethodExecutedCallInterceptor {
    public void methodExecuted(String className, String methodCall, Long contextIdentifier, String threadName, long threadIdentifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long enterMethodTime, long exitMethodTime);
}
