package fr.xebia.profiling.interceptor;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;

public class Compiler {

    private HashMap<String, byte[]> classCompiled = new HashMap<String, byte[]>();

    private ClassLoader cl = new ClassLoader() {

        public synchronized Class loadClass(String className, boolean resolveIt)
                throws ClassNotFoundException {

            Class loadedClass;
            /* Check with the primordial class loader */
            try {
                loadedClass = super.findSystemClass(className);
                return loadedClass;
            } catch (ClassNotFoundException e) {
            }

            byte[] classInByte = classCompiled.get(className);
            loadedClass = defineClass(classInByte, 0, classInByte.length);
            if (loadedClass == null) {
                throw new ClassFormatError();
            }

            if (resolveIt) {
                resolveClass(loadedClass);
            }
            return loadedClass;

        }
    };

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager fileManager =
            compiler.getStandardFileManager(null, null, null);

    public Compiler() throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                Arrays.asList(new File("instrumentation/src/test/compiler-output")));
    }

    public void compileSource(File sourceFile, String className) throws IOException {
        // Compile the file
        compiler.getTask(null,
                fileManager,
                null,
                null,
                null,
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile)))
                .call();
        // Load class in map
        FileInputStream fi = new FileInputStream(new File("instrumentation/src/test/compiler-output/" + className.replace('.', '/') + ".class"));
        byte[] classByte = new byte[fi.available()];
        fi.read(classByte);
        classCompiled.put(className, classByte);
    }

    @SuppressWarnings("unchecked")
    public Class loadClassInVm(String classname) throws ClassNotFoundException {
        Class cls = cl.loadClass(classname);
        return cls;
    }

    public byte[] loadClassInByte(String className) {
        return classCompiled.get(className);
    }

    public void storeClassInByte(String className, byte[] classInByte) {
        classCompiled.put(className, classInByte);
    }

}