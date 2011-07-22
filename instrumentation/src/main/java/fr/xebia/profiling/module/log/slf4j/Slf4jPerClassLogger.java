package fr.xebia.profiling.module.log.slf4j;

import fr.xebia.profiling.interceptor.AdviceMethodCallInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Slf4jPerClassLogger implements AdviceMethodCallInterceptor {

    private Map<String, Logger> classLogger = new ConcurrentHashMap<String, Logger>();

    @Override
    public void enterMethod(String className, String methodCall, String threadName, String identifier, Class[] paramType, Object[] paramValue) {
        Logger logger = getOrCreateLogger(className);
        logger.info("Enter in method {} with parameter {} of type {}, thread {}, id : {}", new Object[]{methodCall, threadName, identifier, constructValue(paramValue), constructType(paramType)});
    }

    @Override
    public void exitMethod(String className, String methodCall, long executionTime) {

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
