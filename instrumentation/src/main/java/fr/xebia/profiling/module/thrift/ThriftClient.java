package fr.xebia.profiling.module.thrift;

import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;

public class ThriftClient implements MethodExecutedCallInterceptor {

    @Override
    public void methodExecuted(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long executionTimeInNano) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
