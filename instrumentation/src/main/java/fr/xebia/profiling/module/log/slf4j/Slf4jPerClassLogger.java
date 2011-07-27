package fr.xebia.profiling.module.log.slf4j;


import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class Slf4jPerClassLogger implements MethodExecutedCallInterceptor {
    private Map<String, Logger> classLogger = new ConcurrentHashMap<String, Logger>();

    @Override

    public void methodExecuted(String className, String methodCall, String threadName, long threadIdentifier, String[] paramType, String[] paramValue, String returnType, String returnValue, long enterMethodTime, long exitMethodTime) {
        Logger logger = getOrCreateLogger(className);
        logger.info("Method {}.{} with parameter {} of type {}, thread {}, id : {}", new Object[]{className, methodCall, constructValue(paramValue), constructType(paramType), threadName, threadIdentifier});
    }

    private Logger getOrCreateLogger(String className) {
        Logger res = classLogger.get(className);
        if (res == null) {
            res = LoggerFactory.getLogger(className);
            classLogger.put(className, res);
        }
        return res;
    }

    private String constructValue(Object[] paramValue) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < paramValue.length; i++) {
            sb.append(paramValue[i]);
            if (i + 1 < paramValue.length) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String constructType(Object[] paramType) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < paramType.length; i++) {
            sb.append(paramType[i]);
            if (i + 1 < paramType.length) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
