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

package fr.jbu.profiling.agent;

import fr.jbu.profiling.configuration.ClassInstrumentationConfiguration;
import fr.jbu.profiling.configuration.MockConfigurationLoader;

import java.lang.instrument.Instrumentation;

public class MainAgent {

    /**
     * Used when started with command line
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        prepareInstrumentation(inst);
    }

    /**
     * Used when started after JVM
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {

    }


    private static void prepareInstrumentation(Instrumentation inst) {
        ClassInstrumentationConfiguration classInstrumentationConfiguration = new ClassInstrumentationConfiguration(new MockConfigurationLoader());
        InstrumentationManager instrumentationManager = new InstrumentationManager(inst, classInstrumentationConfiguration.getPatternForLog());

    }


}
