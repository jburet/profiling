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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;


public class InterceptorTransformer implements Transformer {

    public byte[] transform(byte[] classByteBuffer){
        ClassReader classReader = new ClassReader(classByteBuffer);
        ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        classReader.accept(new InterceptorClassAdapter(cw), ClassReader.SKIP_FRAMES);
        return cw.toByteArray();
    }
}
