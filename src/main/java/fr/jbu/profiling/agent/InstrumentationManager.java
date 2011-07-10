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

import fr.jbu.profiling.configuration.ClassPattern;
import fr.jbu.profiling.interceptor.InterceptorRegistry;
import fr.jbu.profiling.interceptor.InterceptorTransformer;
import fr.jbu.profiling.interceptor.Transformer;
import fr.jbu.profiling.module.log.slf4j.Slf4jPerClassLogger;
import fr.jbu.profiling.module.profiling.sampling.SamplingMethod;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class InstrumentationManager {

    private List<Transformer> transformers = new ArrayList<Transformer>();

    public InstrumentationManager(Instrumentation instrumentation, ClassPattern pattern) {
        transformers.add(new InterceptorTransformer());
        instrumentation.addTransformer(new ASMTransformationClassFileTransfomer(pattern, transformers));
        InterceptorRegistry.registerMethodInterceptor(new Slf4jPerClassLogger());
        // Sampling method
        SamplingMethod samplingMethod = new SamplingMethod();
        InterceptorRegistry.registerClassLoadingInterceptor(samplingMethod);
        InterceptorRegistry.registerMethodInterceptor(samplingMethod);
    }
}
