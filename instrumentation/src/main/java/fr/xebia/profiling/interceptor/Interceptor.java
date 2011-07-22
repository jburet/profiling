package fr.xebia.profiling.interceptor;

import java.util.ArrayList;
import java.util.List;

public class Interceptor {

    private static List<AdviceMethodCallInterceptor> methodsInterceptorAdvices = new ArrayList<AdviceMethodCallInterceptor>();
    private static List<MethodExecutedCallInterceptor> methodExecutedCallInterceptor = new ArrayList<MethodExecutedCallInterceptor>();
    private static List<ClassLoadingInterceptor> classLoadingInterceptors = new ArrayList<ClassLoadingInterceptor>();

    public static void enterMethod(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue) {
        for (AdviceMethodCallInterceptor mci : methodsInterceptorAdvices) {
            mci.enterMethod(className, methodCall, threadName, identifier, paramType, paramValue);
        }
    }

    public static void exitMethod(String className, String methodCall, long executionTimeInNano) {
        for (AdviceMethodCallInterceptor mci : methodsInterceptorAdvices) {
            mci.exitMethod(className, methodCall, executionTimeInNano);
        }
    }

    public static void methodExecuted(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long executionTimeInNano) {
        for (MethodExecutedCallInterceptor mci : methodExecutedCallInterceptor) {
            mci.methodExecuted(className, methodCall, threadName, identifier, paramType, paramValue, returnType, returnValue, executionTimeInNano);
        }
    }


    public static void loadClass(String className) {
        for (ClassLoadingInterceptor cli : classLoadingInterceptors) {
            cli.loadClass(className);
        }
    }

    public static void executeSqlStatement(String className, String methodCall, String threadName, String identifier, String sql) {

    }

    static void registerMethodInterceptor(AdviceMethodCallInterceptor adviceMethodCallInterceptor) {
        methodsInterceptorAdvices.add(adviceMethodCallInterceptor);
    }

    static void registerClassLoadingInterceptor(ClassLoadingInterceptor classLoadingInterceptor) {
        classLoadingInterceptors.add(classLoadingInterceptor);
    }

}
