package fr.xebia.profiling.agent;

import fr.xebia.profiling.configuration.ClassPattern;
import fr.xebia.profiling.interceptor.Transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

public class ASMTransformationClassFileTransfomer implements ClassFileTransformer {

    private ClassPattern classPattern;
    private List<Transformer> transformers;

    public ASMTransformationClassFileTransfomer(ClassPattern classPattern, List<Transformer> transformers) {
        this.classPattern = classPattern;
        this.transformers = transformers;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] classBuffer = classfileBuffer;
        // check if class must be tranformed
        if (classPattern.isClassNameMatch(className)) {
            // Apply all needed tranformation
            for (Transformer t : transformers) {
                classBuffer = t.transform(classBuffer);
            }
        }
        return classBuffer;
    }
}
