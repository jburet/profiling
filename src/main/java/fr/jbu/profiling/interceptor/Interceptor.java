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

package fr.jbu.profiling.interceptor;

import java.util.ArrayList;
import java.util.List;

public class Interceptor {

    private static List<MethodCallInterceptor> methodsInterceptors = new ArrayList<MethodCallInterceptor>();
    private static List<ClassLoadingInterceptor> classLoadingInterceptors = new ArrayList<ClassLoadingInterceptor>();

    public static void enterMethod(String className, String methodCall, Class[] paramType, Object[] paramValue) {
        for (MethodCallInterceptor mci : methodsInterceptors) {
            mci.enterMethod(className, methodCall, paramType, paramValue);
        }
    }

    public static void exitMethod(String className, String methodCall, long executionTimeInNano) {
        for (MethodCallInterceptor mci : methodsInterceptors) {
            mci.exitMethod(className, methodCall, executionTimeInNano);
        }
    }

    public static void loadClass(String className) {
        for (ClassLoadingInterceptor cli : classLoadingInterceptors) {
            cli.loadClass(className);
        }
    }

    static void registerMethodInterceptor(MethodCallInterceptor methodCallInterceptor) {
        methodsInterceptors.add(methodCallInterceptor);
    }

    static void registerClassLoadingInterceptor(ClassLoadingInterceptor classLoadingInterceptor) {
        classLoadingInterceptors.add(classLoadingInterceptor);
    }

}
