package fr.xebia.profiling.interceptor;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class TestInterceptorMethodVisitorWithArrayArgs {

    public static final String CLASSNAME = "classname";
    public static final String METHOD = "methodClass";
    public static final String THREAD_NAME = "threadName";
    public static final String THREAD_ID = "threadId";
    public static final String PARAM_TYPE = "paramType";
    public static final String PARAM_VALUE = "paramValue";
    public static final String RETURN_TYPE = "returnType";
    public static final String RETURN_VALUE = "returnValue";
    public static final String ENTER_METHOD_TIME = "enterMethodTime";
    public static final String EXIT_METHOD_TIME = "exitMethodTime";
    private Compiler compiler = null;
    private InterceptorTransformer interceptorTransformer;
    private final Map<String, Object> lastMethodIntercepted = new HashMap<String, Object>();


    public TestInterceptorMethodVisitorWithArrayArgs() {
        try {
            compiler = new Compiler();
            interceptorTransformer = new InterceptorTransformer();
            // Register test interceptor
            Interceptor.registerMethodInterceptor(new MethodExecutedCallInterceptor() {
                @Override
                public void methodExecuted(String className, String methodCall, String threadName, long threadIdentifier, Class[] paramType, Object[] paramValue, Class returnType, Object returnValue, long enterMethodTime, long exitMethodTime) {
                    lastMethodIntercepted.put(CLASSNAME, className);
                    lastMethodIntercepted.put(METHOD, methodCall);
                    lastMethodIntercepted.put(THREAD_NAME, threadName);
                    lastMethodIntercepted.put(THREAD_ID, threadIdentifier);
                    lastMethodIntercepted.put(PARAM_TYPE, paramType);
                    lastMethodIntercepted.put(PARAM_VALUE, paramValue);
                    lastMethodIntercepted.put(RETURN_TYPE, returnType);
                    lastMethodIntercepted.put(RETURN_VALUE, returnValue);
                    lastMethodIntercepted.put(ENTER_METHOD_TIME, enterMethodTime);
                    lastMethodIntercepted.put(EXIT_METHOD_TIME, exitMethodTime);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @BeforeTest
    public void initLastMethodIntercepted() {
        lastMethodIntercepted.clear();
    }

    @Test
    public void intrument_method_with_int_arg_array_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithIntArrayArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        int[] arg1 =new int[1];
        int[] arg2 =new int[2];

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{int[].class, int[].class});
        method1.invoke(testedClass.newInstance(), new Object[]{arg1, arg2});
        Thread.sleep(1);
        assertNotNull(lastMethodIntercepted.get(CLASSNAME));
        assertNotNull(lastMethodIntercepted.get(METHOD));
        assertNotNull(lastMethodIntercepted.get(THREAD_NAME));
        assertNotNull(lastMethodIntercepted.get(THREAD_ID));
        assertNotNull(lastMethodIntercepted.get(PARAM_TYPE));
        assertNotNull(lastMethodIntercepted.get(PARAM_VALUE));
        assertNotNull(lastMethodIntercepted.get(RETURN_TYPE));
        assertNull(lastMethodIntercepted.get(RETURN_VALUE));
        assertNotNull(lastMethodIntercepted.get(ENTER_METHOD_TIME));
        assertNotNull(lastMethodIntercepted.get(EXIT_METHOD_TIME));
        assertEquals(lastMethodIntercepted.get(CLASSNAME), classUsedForTest.replace('.', '/'));
        assertEquals(lastMethodIntercepted.get(METHOD), methodToTest);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE)).length, 2);
        // Impossible de réenvoyer les class primitive (int.class) avec asm...
        // On renvoie la class de l'objet correspondant
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], int.class);
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], int.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], int[].class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], int[].class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], arg1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], arg2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_object_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithObjectArrayArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Object o1 = new Object[2];
        Object o2 = new Object[4];
        Method method1 = testedClass.getMethod(methodToTest, new Class[]{Object[].class, Object[].class});
        method1.invoke(testedClass.newInstance(), new Object[]{o1, o2});
        Thread.sleep(1);
        assertNotNull(lastMethodIntercepted.get(CLASSNAME));
        assertNotNull(lastMethodIntercepted.get(METHOD));
        assertNotNull(lastMethodIntercepted.get(THREAD_NAME));
        assertNotNull(lastMethodIntercepted.get(THREAD_ID));
        assertNotNull(lastMethodIntercepted.get(PARAM_TYPE));
        assertNotNull(lastMethodIntercepted.get(PARAM_VALUE));
        assertNotNull(lastMethodIntercepted.get(RETURN_TYPE));
        assertNull(lastMethodIntercepted.get(RETURN_VALUE));
        assertNotNull(lastMethodIntercepted.get(ENTER_METHOD_TIME));
        assertNotNull(lastMethodIntercepted.get(EXIT_METHOD_TIME));
        assertEquals(lastMethodIntercepted.get(CLASSNAME), classUsedForTest.replace('.', '/'));
        assertEquals(lastMethodIntercepted.get(METHOD), methodToTest);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE)).length, 2);
        // Impossible de réenvoyer les class primitive (int.class) avec asm...
        // On renvoie la class de l'objet correspondant
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], int.class);
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], int.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Object[].class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Object[].class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], o1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], o2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_int_arg_multiarray_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithIntMultiArrayArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        int[][][] arg1 =new int[1][3][6];
        int[][][] arg2 =new int[2][2][0];

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{int[][][].class, int[][][].class});
        method1.invoke(testedClass.newInstance(), new Object[]{arg1, arg2});
        Thread.sleep(1);
        assertNotNull(lastMethodIntercepted.get(CLASSNAME));
        assertNotNull(lastMethodIntercepted.get(METHOD));
        assertNotNull(lastMethodIntercepted.get(THREAD_NAME));
        assertNotNull(lastMethodIntercepted.get(THREAD_ID));
        assertNotNull(lastMethodIntercepted.get(PARAM_TYPE));
        assertNotNull(lastMethodIntercepted.get(PARAM_VALUE));
        assertNotNull(lastMethodIntercepted.get(RETURN_TYPE));
        assertNull(lastMethodIntercepted.get(RETURN_VALUE));
        assertNotNull(lastMethodIntercepted.get(ENTER_METHOD_TIME));
        assertNotNull(lastMethodIntercepted.get(EXIT_METHOD_TIME));
        assertEquals(lastMethodIntercepted.get(CLASSNAME), classUsedForTest.replace('.', '/'));
        assertEquals(lastMethodIntercepted.get(METHOD), methodToTest);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE)).length, 2);
        // Impossible de réenvoyer les class primitive (int.class) avec asm...
        // On renvoie la class de l'objet correspondant
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], int.class);
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], int.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], int[][][].class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], int[][][].class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], arg1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], arg2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_multi_array_object_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithObjectMultiArrayArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Object[][][] o1 = new Object[2][7][1];
        Object[][][] o2 = new Object[4][0][0];
        Method method1 = testedClass.getMethod(methodToTest, new Class[]{Object[][][].class, Object[][][].class});
        method1.invoke(testedClass.newInstance(), new Object[]{o1, o2});
        Thread.sleep(1);
        assertNotNull(lastMethodIntercepted.get(CLASSNAME));
        assertNotNull(lastMethodIntercepted.get(METHOD));
        assertNotNull(lastMethodIntercepted.get(THREAD_NAME));
        assertNotNull(lastMethodIntercepted.get(THREAD_ID));
        assertNotNull(lastMethodIntercepted.get(PARAM_TYPE));
        assertNotNull(lastMethodIntercepted.get(PARAM_VALUE));
        assertNotNull(lastMethodIntercepted.get(RETURN_TYPE));
        assertNull(lastMethodIntercepted.get(RETURN_VALUE));
        assertNotNull(lastMethodIntercepted.get(ENTER_METHOD_TIME));
        assertNotNull(lastMethodIntercepted.get(EXIT_METHOD_TIME));
        assertEquals(lastMethodIntercepted.get(CLASSNAME), classUsedForTest.replace('.', '/'));
        assertEquals(lastMethodIntercepted.get(METHOD), methodToTest);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE)).length, 2);
        // Impossible de réenvoyer les class primitive (int.class) avec asm...
        // On renvoie la class de l'objet correspondant
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], int.class);
        //assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], int.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Object[][][].class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Object[][][].class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], o1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], o2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

}
