package test;

import java.io.IOException;
import java.lang.Object;
import java.lang.RuntimeException;

public class MethodWithThrows {

    public void voidMethodthrowRuntimeException() {
        throw new RuntimeException();
    }

    public Object methodthrowRuntimeException() {
        throw new RuntimeException();
    }

    public void voidMethodthrowCheckedException() throws IOException {
        throw new IOException();
    }

    public Object methodthrowCheckedException() throws IOException {
        throw new IOException();
    }

}