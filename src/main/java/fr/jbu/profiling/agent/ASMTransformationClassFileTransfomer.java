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
import fr.jbu.profiling.interceptor.Transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

public class ASMTransformationClassFileTransfomer implements ClassFileTransformer {

    private ClassPattern classPattern;
    private List<Transformer> transformers;

    public ASMTransformationClassFileTransfomer(ClassPattern classPattern, List<Transformer> transformers) {
        this.classPattern = classPattern;
        this.transformers = transformers;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] classBuffer = classfileBuffer;
        // check if class must be tranformed
        if (classPattern.isClassNameMatch(className)) {
            // Apply all needed tranformation
            for (Transformer t : transformers) {
                classBuffer = t.transform(classBuffer);
            }
        }
        return classBuffer;
    }
}
