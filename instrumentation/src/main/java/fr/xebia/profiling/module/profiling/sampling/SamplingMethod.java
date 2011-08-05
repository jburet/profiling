package fr.xebia.profiling.module.profiling.sampling;

import fr.xebia.profiling.interceptor.ClassLoadingInterceptor;
import fr.xebia.profiling.interceptor.AdviceMethodCallInterceptor;
import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SamplingMethod implements MethodExecutedCallInterceptor, ClassLoadingInterceptor {

    private Map<String, Map<String, SamplingInfo>> samplingClassMap = new ConcurrentHashMap<String, Map<String, SamplingInfo>>();

    private SamplingHandler samplingHandler;

    private int updateTime;

    private Runnable updateSampling = new Runnable() {

        @Override
        public void run() {
            Map<String, Map<String, SamplingInfo>> oldMap = samplingClassMap;
            samplingClassMap = new ConcurrentHashMap<String, Map<String, SamplingInfo>>();
            samplingHandler.updateSampling(oldMap);
        }
    };

    public SamplingMethod(SamplingHandler samplingHandler, int updateTime) {
        this.samplingHandler = samplingHandler;
        this.updateTime = updateTime;

        Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

            AtomicInteger counter = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("Sampling consolidation Thread #" + counter.incrementAndGet());
                return t;
            }
        }).scheduleAtFixedRate(updateSampling, this.updateTime, this.updateTime, TimeUnit.SECONDS);
    }

    @Override
    public void loadClass(String className) {
        Map<String, SamplingInfo> methodSampling = samplingClassMap.get(className);
        if (methodSampling == null) {
            methodSampling = new ConcurrentHashMap<String, SamplingInfo>();
            samplingClassMap.put(className, methodSampling);
        }
    }

    @Override
    public void methodExecuted(String className, String methodCall, Long contextIdentifier, String threadName, long threadIdentifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long enterMethodTime, long exitMethodTime) {
        Map<String, SamplingInfo> methodSampling = samplingClassMap.get(className);
        synchronized (methodSampling) {
            SamplingInfo si = methodSampling.get(methodCall);
            if (si == null) {
                si = new SamplingInfo();
                methodSampling.put(methodCall, si);
            }
            si.updateCall(exitMethodTime - enterMethodTime);
        }
    }
}
