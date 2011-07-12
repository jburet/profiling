package fr.xebia.profiling.interceptor;

public class InterceptorRegistry {

    public static void registerMethodInterceptor(MethodCallInterceptor methodCallInterceptor) {
        Interceptor.registerMethodInterceptor(methodCallInterceptor);
    }

    public static void registerClassLoadingInterceptor(ClassLoadingInterceptor classLoadingInterceptor) {
        Interceptor.registerClassLoadingInterceptor(classLoadingInterceptor);
    }

}
