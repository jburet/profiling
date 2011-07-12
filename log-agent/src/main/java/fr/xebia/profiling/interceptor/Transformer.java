package fr.xebia.profiling.interceptor;

public interface Transformer {
    public byte[] transform(byte[] classByteBuffer);
}
