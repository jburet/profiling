package fr.xebia.profiling.interceptor;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class Compiler {

    ClassLoader cl = new URLClassLoader(new URL[]{new URL("file://instrumentation/src/test/compiler-output")});

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager fileManager =
            compiler.getStandardFileManager(null, null, null);

    public Compiler() throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                Arrays.asList(new File("instrumentation/src/test/compiler-output")));
    }

    public void compileSource(File sourceFile) throws IOException {
        // Compile the file
        compiler.getTask(null,
                fileManager,
                null,
                null,
                null,
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile)))
                .call();
        fileManager.close();
    }

    @SuppressWarnings("unchecked")
    public void loadClass(String classname) throws ClassNotFoundException {
        Class cls = cl.loadClass(classname);

    }
}