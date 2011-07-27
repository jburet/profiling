package fr.xebia.profiling.interceptor;

public class InterceptorRegistry {

    public static void registerMethodInterceptor(AdviceMethodCallInterceptor adviceMethodCallInterceptor) {
        Interceptor.registerMethodInterceptor(adviceMethodCallInterceptor);
    }

    public static void registerMethodInterceptor(MethodExecutedCallInterceptor methodExecutedCallInterceptor) {
        Interceptor.registerMethodInterceptor(methodExecutedCallInterceptor);
    }

    public static void registerClassLoadingInterceptor(ClassLoadingInterceptor classLoadingInterceptor) {
        Interceptor.registerClassLoadingInterceptor(classLoadingInterceptor);
    }

}
