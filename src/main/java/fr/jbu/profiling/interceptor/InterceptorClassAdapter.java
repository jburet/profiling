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

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class InterceptorClassAdapter extends ClassAdapter {

    private ClassWriter cw;

    private String className = "not initialized";

    public InterceptorClassAdapter(ClassWriter cw) {
        super(cw);
        this.cw = cw;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
        Interceptor.loadClass(name);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new InterceptorMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions), access, name, desc, signature, exceptions, cw, className);
    }
}
