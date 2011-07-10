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


import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

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

        // add method name in operand stack
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);

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

        // add arg value in operand stack
        //mv.visitIntInsn(Opcodes.ASTORE, argTypes.length + 1);
        //mv.visitIntInsn(Opcodes.ALOAD, argTypes.length + 1);


        // Log enter in code
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "fr/xebia/introlog/interceptor/Interceptor", "enterMethod", "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;[Ljava/lang/Object;)V");
        // delagate visit code
        mv.visitCode();
    }

    @Override
    protected void onMethodExit(int opcode) {
        // add method name in operand stack
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                "nanoTime", "()J");
        mv.visitVarInsn(LLOAD, timerLocalVar);
        mv.visitInsn(LSUB);
        // Log exit method in code
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "fr/xebia/introlog/interceptor/Interceptor", "exitMethod", "(Ljava/lang/String;Ljava/lang/String;J)V");

    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(maxStack + 3, maxLocals + 0);
    }

    @Override
    public void visitEnd() {
        mv.visitEnd();
    }
}

