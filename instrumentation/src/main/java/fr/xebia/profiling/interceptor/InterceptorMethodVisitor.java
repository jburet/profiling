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
            mv.visitLdcInsn(convertPrimitiveTypeToObjectType(argTypes[i]));
            mv.visitInsn(Opcodes.AASTORE);
        }
        argTypeVar = this.newLocal(Type.getType(Class[].class));
        mv.visitVarInsn(Opcodes.ASTORE, argTypeVar);

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

        // offset for double and long attribute
        int offset = 1;
        for (int i = 0; i < argTypes.length; i++) {
            Type currentType = argTypes[i];
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
            // Manage primitive case
            // If parameter is primitive box it to object type
            if (currentType.equals(Type.BOOLEAN_TYPE)) {
                mv.visitVarInsn(Opcodes.ILOAD, i + offset);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
            } else if (currentType.equals(Type.CHAR_TYPE)) {
                mv.visitVarInsn(Opcodes.ILOAD, i + offset);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            } else if (currentType.equals(Type.BYTE_TYPE)) {
                mv.visitVarInsn(Opcodes.ILOAD, i + offset);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            } else if (currentType.equals(Type.SHORT_TYPE)) {
                mv.visitVarInsn(Opcodes.ILOAD, i + offset);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
            } else if (currentType.equals(Type.INT_TYPE)) {
                mv.visitVarInsn(Opcodes.ILOAD, i + offset);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            } else if (currentType.equals(Type.LONG_TYPE)) {
                mv.visitVarInsn(Opcodes.LLOAD, i + offset);
                offset++;
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
            } else if (currentType.equals(Type.FLOAT_TYPE)) {
                mv.visitVarInsn(Opcodes.FLOAD, i + offset);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            } else if (currentType.equals(Type.DOUBLE_TYPE)) {
                mv.visitVarInsn(Opcodes.DLOAD, i + offset);
                offset++;
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            } else {
                // Else nothing todo....
                mv.visitVarInsn(Opcodes.ALOAD, i + offset);
            }
            // Store object in array
            mv.visitInsn(Opcodes.AASTORE);
        }
        argValueVar = this.newLocal(Type.getType(Object[].class));
        mv.visitVarInsn(Opcodes.ASTORE, argValueVar);
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
        mv.visitVarInsn(Opcodes.LLOAD, timerLocalVar);

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
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J");

        // Reload argtype and value in stack
        mv.visitVarInsn(Opcodes.ALOAD, argTypeVar);
        mv.visitVarInsn(Opcodes.ALOAD, argValueVar);

        // Put return type in stack
        // case return type is void
        mv.visitLdcInsn(convertPrimitiveTypeToObjectType(returnType));

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "fr/xebia/profiling/interceptor/Interceptor", "methodExecuted", "(" +
                "Ljava/lang/Object;" +
                "J" +
                "J" +
                "Ljava/lang/String;" +
                "Ljava/lang/String;" +
                "Ljava/lang/String;" +
                "J" +
                "[Ljava/lang/Class;" +
                "[Ljava/lang/Object;" +
                "Ljava/lang/Class;" +
                ")V");
    }

    private Type convertPrimitiveTypeToObjectType(Type currentType) {
        if (currentType.equals(Type.VOID_TYPE)) {
            return Type.getType(Void.class);
        } else if (currentType.equals(Type.BOOLEAN_TYPE)) {
            return Type.getType(Boolean.class);
        } else if (currentType.equals(Type.CHAR_TYPE)) {
            return Type.getType(Character.class);
        } else if (currentType.equals(Type.BYTE_TYPE)) {
            return Type.getType(Byte.class);
        } else if (currentType.equals(Type.SHORT_TYPE)) {
            return Type.getType(Short.class);
        } else if (currentType.equals(Type.INT_TYPE)) {
            return Type.getType(Integer.class);
        } else if (currentType.equals(Type.LONG_TYPE)) {
            return Type.getType(Long.class);
        } else if (currentType.equals(Type.FLOAT_TYPE)) {
            return Type.getType(Float.class);
        } else if (currentType.equals(Type.DOUBLE_TYPE)) {
            return Type.getType(Double.class);
        }
        return currentType;
    }

}
