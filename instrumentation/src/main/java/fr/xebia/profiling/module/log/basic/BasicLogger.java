package fr.xebia.profiling.module.log.basic;

import fr.xebia.profiling.interceptor.AdviceMethodCallInterceptor;

public class BasicLogger implements AdviceMethodCallInterceptor {

    public void enterMethod(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue) {
        System.out.println("Enter method : " + methodCall + " of class : " + className);
        for (int i = 0; i < paramType.length; i++) {
            System.out.println("Param type : " + paramType[i]);
            System.out.println("Param value : " + paramValue[i]);
        }

    }

    @Override
    public void exitMethod(String className, String methodCall, long executionTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
