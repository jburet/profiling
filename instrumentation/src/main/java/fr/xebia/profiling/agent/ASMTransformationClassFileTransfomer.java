package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.profiling.interceptor.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

public class ASMTransformationClassFileTransfomer implements ClassFileTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ASMTransformationClassFileTransfomer.class);

    private Instrumentation instrumentation = null;
    private RegExpClassPattern regExpClassPattern;
    private RegExpClassPattern regExpClassToSavePattern;
    private File savePath;
    private List<Transformer> transformers;

    /**
     * @param instrumentation         (Optional, used only id loaded by agent)
     * @param regExpClassPattern
     * @param instrumentedClassToSave
     * @param savePath
     * @param transformers
     */
    public ASMTransformationClassFileTransfomer(Instrumentation instrumentation, RegExpClassPattern regExpClassPattern, RegExpClassPattern instrumentedClassToSave, String savePath, List<Transformer> transformers) {
        this.instrumentation = instrumentation;
        this.regExpClassPattern = regExpClassPattern;
        this.regExpClassToSavePattern = instrumentedClassToSave;
        if (savePath != null) {
            this.savePath = new File(savePath);
        }
        this.transformers = transformers;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] classBuffer = classfileBuffer;
        // check if class must be tranformed
        if (regExpClassPattern.isClassNameMatch(className)) {
            // Apply all needed tranformation
            if (classCanBeModified(classBeingRedefined)) {
                for (Transformer t : transformers) {
                    classBuffer = t.transform(classBuffer);
                }
            } else {
                System.out.println("Class cannot be modified. No instrumentation");
            }

            if (savePath != null && regExpClassToSavePattern != null && regExpClassToSavePattern.isClassNameMatch(className)) {
                File file = new File(savePath, className);
                FileOutputStream fout = null;
                try {
                    file.createNewFile();
                    fout = new FileOutputStream(file);
                    fout.write(classBuffer);
                    fout.close();
                } catch (Exception e) {
                    LOGGER.error("Cannot save instrumented class", e);
                } finally {
                    if (fout != null) {
                        try {
                            fout.close();
                        } catch (IOException e) {
                            LOGGER.warn("Cannot close Stream for file {}", className);
                        }
                    }
                }
            }

        }
        return classBuffer;
    }

    private boolean classCanBeModified(Class<?> classBeingRedefined) {
        
        //if (instrumentation != null) {
        //    return instrumentation.isModifiableClass(classBeingRedefined);
        //}
        return true;
    }
}
