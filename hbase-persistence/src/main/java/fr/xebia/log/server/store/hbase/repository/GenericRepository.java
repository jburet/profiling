package fr.xebia.log.server.store.hbase.repository;

import fr.xebia.log.buffer.CircularBuffer;
import fr.xebia.log.buffer.CircularBufferSizeListener;
import fr.xebia.log.transport.thrift.Log;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericRepository<O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericRepository.class);

    private static final int BUFFER_SIZE = 1000;
    private static final int BATCH_SIZE = 200;

    private CircularBuffer<Put> buffer;

    private CircularBufferSizeListener bufferListener;

    abstract Put convertObjectToPut(O object);

    abstract HTable getTable();

    protected GenericRepository() {
        bufferListener = new CircularBufferSizeListener(BATCH_SIZE) {
            @Override
            public void sizeOverLimitCallback() {
                try {
                    doStoreBuffer(BATCH_SIZE);
                } catch (IOException e) {
                    LOGGER.error("Cannot store batch", e);
                }
            }
        };
        buffer = new CircularBuffer<Put>(BUFFER_SIZE);
        buffer.registerCircularBufferSizeListener(bufferListener);
    }

    private void doStoreBuffer(int batchSize) throws IOException {
        List<Put> puts = new ArrayList<Put>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            puts.add(buffer.dequeue());
        }
        getTable().put(puts);
    }

    /**
     * Store Object
     * Return when object is really stored.
     *
     * @param object
     */
    public void synchronousStore(O object) throws IOException {
        getTable().put(convertObjectToPut(object));
    }

    /**
     * Add O for storing. Really storing on next batch
     *
     * @param object
     */
    public void store(O object) {
        buffer.enqueue(convertObjectToPut(object));
    }
}
