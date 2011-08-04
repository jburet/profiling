package fr.xebia.profiling.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Interceptor {

    private static List<AdviceMethodCallInterceptor> methodsInterceptorAdvices = new ArrayList<AdviceMethodCallInterceptor>();

    private static List<MethodExecutedCallInterceptor> methodExecutedInterceptor = new ArrayList<MethodExecutedCallInterceptor>();

    private static List<ClassLoadingInterceptor> classLoadingInterceptors = new ArrayList<ClassLoadingInterceptor>();

    private static ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {

        AtomicInteger i = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("Interceptor Async #" + i.getAndIncrement());
            return t;
        }
    });

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


    public static void methodExecuted(final Object returnValue, final long enterMethodTime, final long exitMethodTime,
                                      final String className, final String methodCall, final String threadName,
                                      final long threadIdentifier, final Class[] paramType, final Object[] paramValue,
                                      final Class returnType) {
        // Execute in async DON'T BLOCK monitored application code
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (MethodExecutedCallInterceptor mci : methodExecutedInterceptor) {
                    mci.methodExecuted(className, methodCall, threadName, threadIdentifier, paramType, paramValue, returnType, returnValue, enterMethodTime, exitMethodTime);
                }
            }
        });
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

    public static void shutdown(){
        executorService.shutdown();
    }

}
