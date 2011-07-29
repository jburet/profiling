package fr.xebia.profiling.module.thrift;

import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;

public class ThriftClient implements MethodExecutedCallInterceptor {

    @Override
    public void methodExecuted(String className, String methodCall, String threadName, long threadIdentifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long enterMethodTime, long exitMethodTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
