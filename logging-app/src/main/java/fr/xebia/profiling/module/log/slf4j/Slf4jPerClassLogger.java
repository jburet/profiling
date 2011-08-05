package fr.xebia.profiling.module.log.slf4j;


import fr.xebia.profiling.interceptor.MethodExecutedCallInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Slf4jPerClassLogger implements MethodExecutedCallInterceptor {
    private Map<String, Logger> classLogger = new ConcurrentHashMap<String, Logger>();

    /**
     * Only log method with duration upper than limitExecutionTimeInNano
     */
    private long limitExecutionTimeInNano = 0;

    private long timeForInfo = 1 * 1024 * 1024;
    private long timeForWarn = 10 * 1024 * 1024;
    private long timeForError = 100 * 1024 * 1024;

    @Override
    public void methodExecuted(String className, String methodCall, Long contextIdentifier, String threadName, long threadIdentifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long enterMethodTime, long exitMethodTime) {
        Logger logger = getOrCreateLogger(className);
        long executionTimeInNano = (exitMethodTime - enterMethodTime);
        // Log only if > than limitExecutionTimeInNano
        if (executionTimeInNano > limitExecutionTimeInNano) {
            if (paramType.length > 0) {
                logWithDynamicLogLevel(logger, "Method {}( [{}] [{}] ) ==> {} : {} in {} us by thread {}:{}", new Object[]{methodCall, constructType(paramType), constructValue(paramValue), returnType, returnValue, executionTimeInNano / (1000), threadIdentifier, threadName}, executionTimeInNano);
            } else {
                logWithDynamicLogLevel(logger, "Method {} ==> {} : {} in {} us by thread {}:{}", new Object[]{methodCall, returnType, returnValue, (exitMethodTime - enterMethodTime) / (1000), threadIdentifier, threadName}, executionTimeInNano);
            }
        }
    }

    private void logWithDynamicLogLevel(Logger logger, String log, Object[] param, long executionTimeInNano) {
        if (executionTimeInNano > timeForError) {
            logger.error(log, param);
        } else if (executionTimeInNano > timeForWarn) {
            logger.warn(log, param);
        } else if (executionTimeInNano > timeForInfo) {
            logger.info(log, param);
        } else {
            logger.debug(log, param);
        }
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
