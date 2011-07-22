package fr.xebia.profiling.module.profiling.sampling;

import fr.xebia.profiling.interceptor.ClassLoadingInterceptor;
import fr.xebia.profiling.interceptor.AdviceMethodCallInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SamplingMethod implements AdviceMethodCallInterceptor, ClassLoadingInterceptor {

    private Map<String, Map<String, SamplingInfo>> samplingClassMap = new ConcurrentHashMap<String, Map<String, SamplingInfo>>();

    @Override
    public void loadClass(String className) {
        Map<String, SamplingInfo> methodSampling = samplingClassMap.get(className);
        if(methodSampling == null){
            methodSampling = new ConcurrentHashMap<String, SamplingInfo>();
            samplingClassMap.put(className,methodSampling);
        }
    }

    @Override
    public void enterMethod(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue) {

    }

    @Override
    public void exitMethod(String className, String methodCall, long executionTimeInNano) {
        Map<String, SamplingInfo> methodSampling = samplingClassMap.get(className);
        synchronized (methodSampling){
            SamplingInfo si = methodSampling.get(methodCall);
            if(si==null){
                si = new SamplingInfo();
                methodSampling.put(methodCall, si);
            }
            si.updateCall(executionTimeInNano);
        }
    }
}
