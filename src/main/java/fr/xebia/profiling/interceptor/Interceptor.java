package fr.xebia.profiling.interceptor;

import java.util.ArrayList;
import java.util.List;

public class Interceptor {

    private static List<MethodCallInterceptor> methodsInterceptors = new ArrayList<MethodCallInterceptor>();
    private static List<ClassLoadingInterceptor> classLoadingInterceptors = new ArrayList<ClassLoadingInterceptor>();

    public static void enterMethod(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue) {
        for (MethodCallInterceptor mci : methodsInterceptors) {
            mci.enterMethod(className, methodCall, threadName, identifier, paramType, paramValue);
        }
    }

    public static void exitMethod(String className, String methodCall, long executionTimeInNano) {
        for (MethodCallInterceptor mci : methodsInterceptors) {
            mci.exitMethod(className, methodCall, executionTimeInNano);
        }
    }

    public static void loadClass(String className) {
        for (ClassLoadingInterceptor cli : classLoadingInterceptors) {
            cli.loadClass(className);
        }
    }

    static void registerMethodInterceptor(MethodCallInterceptor methodCallInterceptor) {
        methodsInterceptors.add(methodCallInterceptor);
    }

    static void registerClassLoadingInterceptor(ClassLoadingInterceptor classLoadingInterceptor) {
        classLoadingInterceptors.add(classLoadingInterceptor);
    }

}
