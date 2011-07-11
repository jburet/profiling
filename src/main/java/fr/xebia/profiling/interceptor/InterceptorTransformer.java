package fr.xebia.profiling.interceptor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;


public class InterceptorTransformer implements Transformer {

    public byte[] transform(byte[] classByteBuffer){
        ClassReader classReader = new ClassReader(classByteBuffer);
        ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        classReader.accept(new InterceptorClassAdapter(cw), ClassReader.SKIP_FRAMES);
        return cw.toByteArray();
    }
}
