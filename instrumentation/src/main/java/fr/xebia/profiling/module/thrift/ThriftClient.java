package fr.xebia.profiling.module.thrift;

import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;

public class ThriftClient implements MethodExecutedCallInterceptor {
    @Override
    public void methodExecuted(String className, String methodCall, String threadName, long threadIdentifier, String[] paramType, String[] paramValue, String returnType, String returnValue, long enterMethodTime, long exitMethodTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
