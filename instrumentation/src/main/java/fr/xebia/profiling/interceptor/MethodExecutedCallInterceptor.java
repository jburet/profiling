package fr.xebia.profiling.interceptor;


public interface MethodExecutedCallInterceptor {
    public void methodExecuted(String className, String methodCall, String threadName, long threadIdentifier, String[] paramType, String[] paramValue, String returnType, String returnValue, long enterMethodTime, long exitMethodTime);
}
