package fr.xebia.profiling.interceptor;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.UUID;

/**
 * Log enter method at the beginning of the code
 * Log exit method for each return in code
 */

public class InterceptorMethodVisitor extends AdviceAdapter {

    private String[] argTypesString;
    private Type[] argTypes;
    private Type returnType;
    private String className;
    private String methodName;
    private String argDesc;
    private ClassWriter cw;
    private int timerLocalVar;
    private int argTypeVar;
    private int argValueVar;

    /**
     * Constructs a new {@link org.objectweb.asm.MethodAdapter} object.
     *
     * @param mv         the code visitor to which this adapter must delegate calls.
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     */
    public InterceptorMethodVisitor(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions, ClassWriter cw, String className) {
        super(mv, access, name, desc);
        this.argTypes = Type.getArgumentTypes(desc);
        this.argTypesString = getStringsType(argTypes);
        this.returnType = Type.getReturnType(desc);
        this.className = className;
        this.methodName = name;
        this.argDesc = desc;
        this.cw = cw;
    }

    private String[] getStringsType(Type[] argTypes) {
        String[] res = new String[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            res[i] = argTypes[i].getInternalName();
        }
        return res;
    }

    @Override
    public void onMethodEnter() {
        // -- Timer
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                "nanoTime", "()J");
        timerLocalVar = this.newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(Opcodes.LSTORE, timerLocalVar);

        // -- Manage arg type


        // add arg type in operand stack
        // Push array size
        switch (argTypes.length) {
            case 0:
                mv.visitInsn(Opcodes.ICONST_0);
                break;
            case 1:
                mv.visitInsn(Opcodes.ICONST_1);
                break;
            case 2:
                mv.visitInsn(Opcodes.ICONST_2);
                break;
            case 3:
                mv.visitInsn(Opcodes.ICONST_3);
                break;
            case 4:
                mv.visitInsn(Opcodes.ICONST_4);
                break;
            case 5:
                mv.visitInsn(Opcodes.ICONST_5);
                break;
            default:
                mv.visitIntInsn(Opcodes.BIPUSH, argTypes.length);
                break;

        }
        mv.visitTypeInsn(Opcodes.ANEWARRAY, Type.getType(Class.class).getInternalName());
        for (int i = 0; i < argTypesString.length; i++) {
            // Duplicate parameter
            mv.visitInsn(Opcodes.DUP);

            // Create a constant for array position
            switch (i) {
                case 0:
                    mv.visitInsn(Opcodes.ICONST_0);
                    break;
                case 1:
                    mv.visitInsn(Opcodes.ICONST_1);
                    break;
                case 2:
                    mv.visitInsn(Opcodes.ICONST_2);
                    break;
                case 3:
                    mv.visitInsn(Opcodes.ICONST_3);
                    break;
                case 4:
                    mv.visitInsn(Opcodes.ICONST_4);
                    break;
                case 5:
                    mv.visitInsn(Opcodes.ICONST_5);
                    break;
                default:
                    mv.visitIntInsn(Opcodes.BIPUSH, i);
                    break;
            }

            mv.visitLdcInsn(argTypes[i]);
            mv.visitInsn(Opcodes.AASTORE);
        }
        argTypeVar = this.newLocal(Type.getType(Class[].class));
        mv.visitVarInsn(Opcodes.LSTORE, argTypeVar);

        // -- Manage arg value
        // Push array size
        switch (argTypes.length) {
            case 0:
                mv.visitInsn(Opcodes.ICONST_0);
                break;
            case 1:
                mv.visitInsn(Opcodes.ICONST_1);
                break;
            case 2:
                mv.visitInsn(Opcodes.ICONST_2);
                break;
            case 3:
                mv.visitInsn(Opcodes.ICONST_3);
                break;
            case 4:
                mv.visitInsn(Opcodes.ICONST_4);
                break;
            case 5:
                mv.visitInsn(Opcodes.ICONST_5);
                break;
            default:
                mv.visitIntInsn(Opcodes.BIPUSH, argTypes.length);
                break;

        }
        // Create an array with same size as desc
        mv.visitTypeInsn(Opcodes.ANEWARRAY, Type.getType(Object.class).getInternalName());

        for (int i = 0; i < argTypes.length; i++) {
            // Duplicate parameter
            mv.visitInsn(Opcodes.DUP);

            // Create a constant for array position
            switch (i) {
                case 0:
                    mv.visitInsn(Opcodes.ICONST_0);
                    break;
                case 1:
                    mv.visitInsn(Opcodes.ICONST_1);
                    break;
                case 2:
                    mv.visitInsn(Opcodes.ICONST_2);
                    break;
                case 3:
                    mv.visitInsn(Opcodes.ICONST_3);
                    break;
                case 4:
                    mv.visitInsn(Opcodes.ICONST_4);
                    break;
                case 5:
                    mv.visitInsn(Opcodes.ICONST_5);
                    break;
                default:
                    mv.visitIntInsn(Opcodes.BIPUSH, i);
                    break;
            }

            // Load paramater
            mv.visitIntInsn(Opcodes.ALOAD, i + 1);
            mv.visitInsn(Opcodes.AASTORE);
        }
        argValueVar = this.newLocal(Type.getType(Object[].class));
        mv.visitVarInsn(Opcodes.LSTORE, argValueVar);
    }

    @Override
    protected void onMethodExit(int opcode) {
        // Dup value if exist or put null
        if (opcode == RETURN) {
            visitInsn(ACONST_NULL);
        } else if (opcode == ARETURN || opcode == ATHROW) {
            dup();
        } else {
            if (opcode == LRETURN || opcode == DRETURN) {
                dup2();
            } else {
                dup();
            }
        }
        // Reload start time from localvariable
        mv.visitVarInsn(Opcodes.ALOAD, timerLocalVar);

        // End time
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                "nanoTime", "()J");

        // add method name, classname, threadname, correlation id in operand stack
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        // Thread name
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getName", "()Ljava/lang/String;");

        // Thread identifier
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J;");

        // Reload argtype and value in stack
        mv.visitVarInsn(Opcodes.LLOAD, argTypeVar);
        mv.visitVarInsn(Opcodes.LLOAD, argValueVar);

        // Put return type in stack
        mv.visitLdcInsn(returnType);

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "fr/xebia/profiling/interceptor/Interceptor", "methodExecuted", "(" +
                "Ljava/lang/Object;" +
                "J;" +
                "J;" +
                "Ljava/lang/String;" +
                "Ljava/lang/String;" +
                "Ljava/lang/String;" +
                "J;" +
                "[Ljava/lang/Class;" +
                "[Ljava/lang/Object;" +
                "Ljava/lang/Class;" +
                ")V");
    }
}
