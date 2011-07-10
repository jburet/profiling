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

package fr.jbu.profiling.module.log.basic;

import fr.jbu.profiling.interceptor.MethodCallInterceptor;

public class BasicLogger implements MethodCallInterceptor {

    public void enterMethod(String className, String methodCall, Class[] paramType, Object[] paramValue) {
        System.out.println("Enter method : " + methodCall + " of class : " + className);
        for (int i = 0; i < paramType.length; i++) {
            System.out.println("Param type : " + paramType[i]);
            System.out.println("Param value : " + paramValue[i]);
        }

    }

    @Override
    public void exitMethod(String className, String methodCall, long executionTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
