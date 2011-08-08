package test;

import java.lang.Object;

public class MethodWithReturn {

    public boolean returnBoolean() {
        return false;
    }

    public char returnChar() {
        return 'a';
    }

    public byte returnByte() {
        return (byte) 1;
    }

    public short returnShort() {
        return (short) 1;
    }

    public int returnInt() {
        return 1;
    }

    public long returnLong() {
        return (long) 1;
    }

    public float returnFloat() {
        return (float) 1.0;
    }

    public double returnDouble() {
        return (double) 1.0;
    }

    public Object returnObject() {
        return fr.xebia.profiling.interceptor.TestInterceptorMethodVisitorReturn.TEST_OBJECT;
    }

    public int[] returnIntArray() {
        return new int[0];
    }

    public Object[] returnObjectArray() {
        return fr.xebia.profiling.interceptor.TestInterceptorMethodVisitorReturn.TEST_OBJECT_ARRAY;
    }
}