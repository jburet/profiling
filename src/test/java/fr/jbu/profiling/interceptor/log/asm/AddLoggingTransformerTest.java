/**
 *     Log, profiling based on Java Agent
 *     Copyright (C) 2011  Julien Buret
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.jbu.profiling.interceptor.log.asm;

import fr.jbu.profiling.interceptor.InterceptorTransformer;
import org.testng.annotations.Test;

import java.io.*;


public class AddLoggingTransformerTest {

    private InterceptorTransformer interceptorTransformer = new InterceptorTransformer();

    @Test
    public void testAddLog() throws Exception {
        // read a class and transform them to byte array
        byte[] byteClass = getBytesFromFile(new File("src/test/test-classes/SimpleClasses.class"));
        byte[] res = interceptorTransformer.transform(byteClass);
        createFileFromByte(new File("src/test/test-classes/out/SimpleClasses.class"), res);
        // Reload class in ASM

    }


    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();


        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    public static void createFileFromByte(File file, byte[] content) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(content);
        fout.flush();
        fout.close();
    }

}
