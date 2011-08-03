package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.profiling.interceptor.Transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

public class ASMTransformationClassFileTransfomer implements ClassFileTransformer {

    private RegExpClassPattern regExpClassPattern;
    private List<Transformer> transformers;

    public ASMTransformationClassFileTransfomer(RegExpClassPattern regExpClassPattern, List<Transformer> transformers) {
        this.regExpClassPattern = regExpClassPattern;
        this.transformers = transformers;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] classBuffer = classfileBuffer;
        // check if class must be tranformed
        if (regExpClassPattern.isClassNameMatch(className)) {
            // Apply all needed tranformation
            for (Transformer t : transformers) {
                classBuffer = t.transform(classBuffer);
            }
        }
        return classBuffer;
    }
}
