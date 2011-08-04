package fr.xebia.profiling.interceptor;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestInterceptorMethodWithInternalClass {


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


    public TestInterceptorMethodWithInternalClass() {
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
    public void instrument_method_with_return_internal_def_class_valid() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithInternalClass";
        final String methodToTest = "test1";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{});
        method1.invoke(testedClass.newInstance(), new Object[]{});


    }

    @Test
    public void instrument_method_with_return_internal_def_class_valid_2() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithInternalClass";
        final String methodToTest = "test2";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{});
        method1.invoke(testedClass.newInstance(), new Object[]{});


    }

    @Test
    public void instrument_method_with_return_internal_def_class_valid_3() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithInternalClass";
        final String methodToTest = "test3";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(classUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(classUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{});
        method1.invoke(testedClass.newInstance(), new Object[]{});


    }

    @Test
    public void instrument_method_with_return_internal_def_class_valid_4() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        // compile simple class
        final String classUsedForTest = "test.MethodWithInternalClass";
        final String innerClassUsedForTest = "test.MethodWithInternalClass$InnerClass";
        final String methodToTest = "test1";

        compiler.compileSource(new File("instrumentation/src/test/sources/" + classUsedForTest.replace('.', '/') + ".java"), classUsedForTest);
        // Instrument compiled class
        byte[] classInByte = compiler.loadClassInByte(innerClassUsedForTest);
        classInByte = interceptorTransformer.transform(classInByte);
        compiler.storeClassInByte(innerClassUsedForTest, classInByte);
        Class testedClass = compiler.loadClassInVm(classUsedForTest);

        Method method1 = testedClass.getMethod(methodToTest, new Class[]{});
        method1.invoke(testedClass.newInstance(), new Object[]{});


    }

}
