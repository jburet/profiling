package fr.xebia.profiling.interceptor;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class TestInterceptorMethodVisitor {

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


    public TestInterceptorMethodVisitor() {
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
    public void intrument_simplest_method_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        compiler.compileSource(new File("instrumentation/src/test/sources/test/SimpleClass.java"), "test.SimpleClass");
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte("test.SimpleClass");
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte("test.SimpleClass", classInByte);
        Class testedClass = compiler.loadClassInVm("test.SimpleClass");
        Method method1 = method1 = testedClass.getMethod("method1");
        method1.invoke(testedClass.newInstance(), new Object[0]);
        Thread.sleep(100);
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
        assertEquals(lastMethodIntercepted.get(CLASSNAME), "test/SimpleClass");
        assertEquals(lastMethodIntercepted.get(METHOD), "method1");
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE)).length, 0);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 0);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_int_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithIntArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{int.class, int.class});
        method1.invoke(testedClass.newInstance(), new Object[]{1, 2});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Integer.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Integer.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], 1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], 2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_char_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithCharArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{char.class, char.class});
        method1.invoke(testedClass.newInstance(), new Object[]{'a', 'b'});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Character.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Character.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], 'a');
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], 'b');
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }


    @Test
    public void intrument_method_with_boolean_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithBooleanArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{boolean.class, boolean.class});
        method1.invoke(testedClass.newInstance(), new Object[]{true, false});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Boolean.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Boolean.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], true);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], false);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }


    @Test
    public void intrument_method_with_short_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithShortArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{short.class, short.class});
        method1.invoke(testedClass.newInstance(), new Object[]{(short) 1, (short) 2});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Short.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Short.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], (short) 1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], (short) 2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_long_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithLongArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{long.class, long.class});
        method1.invoke(testedClass.newInstance(), new Object[]{(long) 1, (long) 2});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Long.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Long.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], (long) 1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], (long) 2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_float_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithFloatArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{float.class, float.class});
        method1.invoke(testedClass.newInstance(), new Object[]{(float) 1, (float) 2});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Float.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Float.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], (float) 1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], (float) 2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }

    @Test
    public void intrument_method_with_double_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithDoubleArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{double.class, double.class});
        method1.invoke(testedClass.newInstance(), new Object[]{(double) 1, (double) 2});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Double.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Double.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], (double) 1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], (double) 2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }


    @Test
    public void intrument_method_with_object_arg_in_public_class() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithArg";
        final String methodToTest = "methodWithObjectArg";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Object o1 = new Object();
        Object o2 = new Object();
        Method method1 = testedClass.getMethod(methodToTest, new Class[]{Object.class, Object.class});
        method1.invoke(testedClass.newInstance(), new Object[]{o1, o2});
        Thread.sleep(100);
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
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[0], Object.class);
        assertEquals(((Class[]) lastMethodIntercepted.get(PARAM_TYPE))[1], Object.class);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE)).length, 2);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[0], o1);
        assertEquals(((Object[]) lastMethodIntercepted.get(PARAM_VALUE))[1], o2);
        assertEquals(lastMethodIntercepted.get(RETURN_TYPE), Void.class);

    }


}
