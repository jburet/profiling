package fr.xebia.profiling.interceptor;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Compiler {

    private HashMap<String, byte[]> classCompiled = new HashMap<String, byte[]>();

    private ClassLoader cl = new ClassLoader() {

        ConcurrentHashMap<String, Class> loadedClasses = new ConcurrentHashMap<String, Class>();

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
            if (loadedClasses.get(className) != null) {
                return loadedClasses.get(className);
            } else {

                loadedClass = defineClass(classInByte, 0, classInByte.length);
                loadedClasses.put(className, loadedClass);
                if (loadedClass == null) {
                    throw new ClassFormatError();
                }

                if (resolveIt) {
                    resolveClass(loadedClass);
                }
                return loadedClass;
            }
        }
    };

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager fileManager =
            compiler.getStandardFileManager(null, null, null);

    public Compiler() throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        File output = new File("instrumentation/src/test/compiler-output");
        if (!output.exists()) {
            output.mkdir();
        }
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                Arrays.asList(output));
    }

    public void compileSource(File sourceFile, final String className) throws IOException {
        // Compile the file
        JavaCompiler.CompilationTask task = compiler.getTask(null,
                fileManager,
                null,
                null,
                null,
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile)));
        if (task.call()) {
            // Load class in map
            // Create a file for class
            // package
            String filePath = "instrumentation/src/test/compiler-output/" + className.replace(".", "/");
            File file = new File(filePath.substring(0, filePath.lastIndexOf("/")));
            // Search all file with name beginning with classname
            for (File matchedFile : file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(className.substring(className.lastIndexOf(".") + 1));
                }
            })) {
                FileInputStream fi = new FileInputStream(matchedFile);
                byte[] classByte = new byte[fi.available()];
                fi.read(classByte);
                // Retrieve full classname
                classCompiled.put(className.substring(0, className.lastIndexOf(".") + 1) + matchedFile.getName().replace(".class", ""), classByte);
                fi.close();
            }
        } else {
            throw new RuntimeException();
        }
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
        File file = new File(className);
        FileOutputStream fout = null;
        try {
            file.createNewFile();
            fout = new FileOutputStream(file);
            fout.write(classInByte);
            fout.close();
        } catch (Exception e) {

        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }
}