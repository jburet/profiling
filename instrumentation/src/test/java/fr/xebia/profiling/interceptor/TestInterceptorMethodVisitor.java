package fr.xebia.profiling.interceptor;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class TestInterceptorMethodVisitor {

    private Compiler compiler = null;

    public TestInterceptorMethodVisitor() {
        try {
            compiler = new Compiler();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testAddSimpleLogging() throws IOException, ClassNotFoundException {
        // compile simple class
        compiler.compileSource(new File("instrumentation/src/test/sources/test/SimpleClass.java"));
        compiler.loadClass("test.SimpleClass");
        
    }

}
