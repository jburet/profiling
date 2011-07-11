package fr.xebia.profiling.interceptor;

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
