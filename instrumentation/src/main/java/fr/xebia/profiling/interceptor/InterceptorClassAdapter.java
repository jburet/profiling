package fr.xebia.profiling.interceptor;

import fr.xebia.profiling.interceptor.method.InterceptorMethodVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Intrument classes
 * If class is synthetic do nothing
 */
public class InterceptorClassAdapter extends ClassAdapter {

    private ClassWriter cw;

    private String className = "not initialized";

    private boolean isSynthetic = false;

    public InterceptorClassAdapter(ClassWriter cw) {
        super(cw);
        this.cw = cw;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
        // If not synthetic instrument
        if ((access & Opcodes.ACC_SYNTHETIC) == 0 && (access & Opcodes.ACC_INTERFACE) == 0) {
            Interceptor.loadClass(name);
        }else{
            isSynthetic = true;
        }

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // Don't intrument synthetic class
        if(isSynthetic){
            return super.visitMethod(access, name, desc, signature, exceptions);
        }else{
            return new InterceptorMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions), access, name, desc, signature, exceptions, cw, className);
        }

    }
}
