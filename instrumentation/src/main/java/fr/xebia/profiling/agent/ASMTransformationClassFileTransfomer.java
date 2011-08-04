package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.profiling.interceptor.Transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                /**
                 * TODO make a better implem
                 * For debug purpose
                 */
                String f = className.substring(className.lastIndexOf("/"));
                System.out.println("create : " + f);
                File file = new File(f);
                FileOutputStream fout = null;
                try {
                    file.createNewFile();
                    fout = new FileOutputStream(file);
                    fout.write(classBuffer);
                    fout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fout != null) {
                        try {
                            fout.close();
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
                /**
                 * End debug
                 */
            }
        }


        return classBuffer;
    }
}
