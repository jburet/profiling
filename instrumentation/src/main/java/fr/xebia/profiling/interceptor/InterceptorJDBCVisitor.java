package fr.xebia.profiling.interceptor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class InterceptorJDBCVisitor extends LocalVariablesSorter {


    public InterceptorJDBCVisitor(MethodVisitor methodVisitor, int access, String desc) {
        super(access, desc, methodVisitor);
    }

    @Override
    public void visitCode() {
        mv.visitIntInsn(Opcodes.ALOAD, 1);
        super.visitCode();
    }
}
