/**
 *     Log, profiling based on Java Agent
 *     Copyright (C) 2011  Julien Buret
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.jbu.profiling.module.log.slf4j;

import fr.jbu.profiling.interceptor.MethodCallInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Slf4jPerClassLogger implements MethodCallInterceptor {

    private Map<String, Logger> classLogger = new ConcurrentHashMap<String, Logger>();

    @Override
    public void enterMethod(String className, String methodCall, Class[] paramType, Object[] paramValue) {
        Logger logger = getOrCreateLogger(className);
        logger.info("Enter in method {} with parameter {} of type {} ", new Object[]{methodCall, constructValue(paramValue), constructType(paramType)});
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
