package fr.xebia.log;

import fr.xebia.log.buffer.CircularBuffer;
import fr.xebia.log.buffer.CircularBufferSizeListener;
import fr.xebia.log.buffer.NoElementException;
import fr.xebia.log.buffer.OverflowException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestCircularBuffer {

    @Test
    public void when_I_have_empty_buffer_then_size_is_zero() {
        CircularBuffer<Object> cb = new CircularBuffer();
        assertEquals(0, cb.size());
    }

    @Test
    public void when_I_enqueue_an_element_then_size_is_1() {
        CircularBuffer<Object> cb = new CircularBuffer();
        cb.enqueue(new Object());
        assertEquals(cb.size(), 1);
    }

    @Test
    public void when_I_enqueue_an_Element_in_buffer_Then_I_can_dequeue_this_element() {
        CircularBuffer<Object> cb = new CircularBuffer();
        Object obj = new Object();
        assertEquals(cb.size(), 0);
        cb.enqueue(obj);
        assertEquals(cb.size(), 1);
        assertEquals(obj, cb.dequeue());
        assertEquals(cb.size(), 0);
    }

    @Test
    public void when_I_enqueue_two_element_Then_I_dequeue_the_first_element() {
        CircularBuffer<Object> cb = new CircularBuffer();
        Object obj1 = new Object();
        assertEquals(cb.size(), 0);
        cb.enqueue(obj1);
        assertEquals(cb.size(), 1);
        Object obj2 = new Object();
        cb.enqueue(obj2);
        assertEquals(cb.size(), 2);
        assertEquals(obj1, cb.dequeue());
        assertEquals(cb.size(), 1);
    }

    @Test
    public void when_I_enqueu_and_dequeu_more_time_than_buffer_size_its_work() {
        CircularBuffer<Object> cb = new CircularBuffer(2);
        Object obj1 = new Object();
        cb.enqueue(obj1);
        cb.dequeue();
        cb.enqueue(obj1);
        cb.dequeue();
        cb.enqueue(obj1);
        cb.dequeue();
        cb.enqueue(obj1);
        cb.dequeue();
    }

    @Test(expectedExceptions = OverflowException.class)
    public void when_I_enqueu_in_full_buffer_overflow_exeption_is_throwed() {
        CircularBuffer<Object> cb = new CircularBuffer(2);
        Object obj1 = new Object();
        cb.enqueue(obj1);
        cb.enqueue(obj1);
        cb.enqueue(obj1);
    }

    @Test(expectedExceptions = NoElementException.class)
    public void when_I_enqueu_an_empty_queue_no_element_exeption_is_throwed() {
        CircularBuffer<Object> cb = new CircularBuffer(2);
        cb.dequeue();
    }

    @Test
    public void when_I_register_listner_and_exceed_limit_then_callback_is_called() {
        CircularBuffer<Object> cb = new CircularBuffer();
        final CallbackResult callbackResult = new CallbackResult();
        CircularBufferSizeListener listener = new CircularBufferSizeListener(2) {
            @Override
            public void sizeOverLimitCallback() {
                callbackResult.setCallbackCalled();
            }
        };
        cb.registerCircularBufferSizeListener(listener);
        cb.enqueue(new Object());
        cb.enqueue(new Object());
        cb.enqueue(new Object());
        assertTrue(callbackResult.isCallbackCalled());
    }


}

class CallbackResult {

    private boolean callbackCalled = false;

    public void setCallbackCalled() {
        callbackCalled = true;
    }

    public boolean isCallbackCalled() {
        return callbackCalled;
    }

}