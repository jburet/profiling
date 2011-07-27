package fr.xebia.profiling.interceptor;

import java.util.ArrayList;
import java.util.List;

public class Interceptor {

    private static List<AdviceMethodCallInterceptor> methodsInterceptorAdvices = new ArrayList<AdviceMethodCallInterceptor>();

    private static List<MethodExecutedCallInterceptor> methodExecutedInterceptor = new ArrayList<MethodExecutedCallInterceptor>();

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


    public static void methodExecuted(String className, String methodCall, String threadName, long threadIdentifier, String[] paramType, String[] paramValue, String returnType, String returnValue, long enterMethodTime, long exitMethodTime) {
        for (MethodExecutedCallInterceptor mci : methodExecutedInterceptor) {
            mci.methodExecuted(className, methodCall, threadName, threadIdentifier, paramType, paramValue, returnType, returnValue, enterMethodTime, exitMethodTime);
        }
    }


    public static void loadClass(String className) {
        for (ClassLoadingInterceptor cli : classLoadingInterceptors) {
            cli.loadClass(className);
        }
    }


    static void registerMethodInterceptor(AdviceMethodCallInterceptor adviceMethodCallInterceptor) {
        methodsInterceptorAdvices.add(adviceMethodCallInterceptor);
    }

    static void registerMethodInterceptor(MethodExecutedCallInterceptor methodExecutedCallInterceptor) {
        methodExecutedInterceptor.add(methodExecutedCallInterceptor);
    }

    static void registerClassLoadingInterceptor(ClassLoadingInterceptor classLoadingInterceptor) {
        classLoadingInterceptors.add(classLoadingInterceptor);
    }

}
