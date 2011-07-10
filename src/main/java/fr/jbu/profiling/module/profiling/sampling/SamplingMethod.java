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

package fr.jbu.profiling.module.profiling.sampling;

import fr.jbu.profiling.interceptor.ClassLoadingInterceptor;
import fr.jbu.profiling.interceptor.MethodCallInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SamplingMethod implements MethodCallInterceptor, ClassLoadingInterceptor {

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
    public void enterMethod(String className, String methodCall, Class[] paramType, Object[] paramValue) {

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
