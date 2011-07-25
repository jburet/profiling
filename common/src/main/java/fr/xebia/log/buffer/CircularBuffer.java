package fr.xebia.log.buffer;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.util.ArrayList;
import java.util.List;

/**
 * No MT implementation of circularBuffer
 * (One thread write, one thread read)
 * http://en.wikipedia.org/wiki/Circular_buffer
 */
public class CircularBuffer<T> {

    private Object[] internalArray;
    private int size;
    private int readIndex;
    private int writeIndex;

    private List<CircularBufferSizeListener> listeners;

    public CircularBuffer() {
        this(500);
    }

    public CircularBuffer(int bufferSize) {
        this.listeners = new ArrayList<CircularBufferSizeListener>();
        this.internalArray = new Object[bufferSize];
        this.readIndex = 0;
        this.writeIndex = 0;
        this.size = bufferSize;
    }

    public void enqueue(T obj) {
        int futureWriteIndex = (this.writeIndex + 1) % size;
        if (futureWriteIndex == readIndex) {
            throw new OverflowException();
        }
        this.writeIndex = futureWriteIndex;
        this.internalArray[this.writeIndex] = obj;
        // Verify listener
        int currentSize = size();
        for (CircularBufferSizeListener listener : listeners) {
            if (listener.isSizeOver(currentSize)) {
                listener.sizeOverLimitCallback();
            }
        }
    }

    public T dequeue() {
        int futureReadIndex = (this.readIndex + 1) % size;
        if (futureReadIndex > writeIndex) {
            throw new NoElementException();
        }
        this.readIndex = futureReadIndex;
        return (T) this.internalArray[this.readIndex];
    }

    public int size() {
        if (writeIndex >= readIndex) {
            return this.writeIndex - this.readIndex;
        } else {
            return this.writeIndex + this.size - this.readIndex;
        }
    }

    public void registerCircularBufferSizeListener(CircularBufferSizeListener listener) {
        this.listeners.add(listener);
    }


}

