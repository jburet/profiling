package fr.xebia.profiling.agent;

import fr.xebia.log.configuration.RegExpClassPattern;
import fr.xebia.profiling.interceptor.Transformer;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

public class TestASMTransformationClassFileTransfomer {

    @Test
    public void when_transform_class_bytecode_with_pattern_match_byte_code_is_intrumented() throws IllegalClassFormatException {
        RegExpClassPattern intrumentPattern = new RegExpClassPattern(Arrays.asList(new String[]{"fr.xebia.*"}));
        byte[] classToTransform = new byte[0];

        // Mock transformer
        Transformer mockTransformer = mock(Transformer.class);

        ASMTransformationClassFileTransfomer asmTransformationClassFileTransfomer = new ASMTransformationClassFileTransfomer(null, intrumentPattern, null, null,
                Arrays.asList(new Transformer[]{mockTransformer}));
        asmTransformationClassFileTransfomer.transform(null, "fr.xebia.MyClass", Double.class, null, classToTransform);

        verify(mockTransformer).transform(classToTransform);
    }

    @Test
    public void when_transform_class_bytecode_with_save_pattern_match_byte_code_is_saved_in_saved_path() throws IllegalClassFormatException {
        RegExpClassPattern intrumentPattern = new RegExpClassPattern(Arrays.asList(new String[]{"fr.xebia.*"}));
        RegExpClassPattern savePattern = new RegExpClassPattern(Arrays.asList(new String[]{"fr.xebia.*"}));
        byte[] classToTransform = new byte[0];

        // Mock transformer
        Transformer mockTransformer = mock(Transformer.class);
        when(mockTransformer.transform(classToTransform)).thenReturn("test".getBytes());

        ASMTransformationClassFileTransfomer asmTransformationClassFileTransfomer = new ASMTransformationClassFileTransfomer(null, intrumentPattern, savePattern,
                "./",
                Arrays.asList(new Transformer[]{mockTransformer}));
        asmTransformationClassFileTransfomer.transform(null, "fr.xebia.MyClass", Double.class, null, classToTransform);

        //assert
        File resFile = new File("./" + "fr.xebia.MyClass");
        assertTrue(resFile.exists());

    }

}
