package fr.xebia.log.server.store.hbase.repository;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class GenericRepository<O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericRepository.class);

    private static final int BUFFER_SIZE = Integer.MAX_VALUE;

    private static PersistenceScheduler persistenceScheduler = new PersistenceScheduler();

    private LinkedBlockingQueue<Put> buffer;

    abstract Put convertObjectToPut(O object);

    abstract HTable getTable();

    protected GenericRepository() {
        buffer = new LinkedBlockingQueue<Put>(BUFFER_SIZE);
        persistenceScheduler.registerRepository(this);
    }

    private void doStoreBuffer() throws IOException {
        doStoreBuffer(buffer.size());
    }

    private void doStoreBuffer(int batchSize) throws IOException {
        List<Put> puts = new ArrayList<Put>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            puts.add(buffer.poll());
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
        if (!buffer.offer(convertObjectToPut(object))) {
            LOGGER.error("Cannot store object in buffer. Max size Exceeded ");
        }
    }

    static class PersistenceScheduler {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            int counter = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("Hbase async store #" + counter++);
                return t;
            }
        });

        private List<GenericRepository> repositories = new CopyOnWriteArrayList<GenericRepository>();

        PersistenceScheduler() {
            scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    for (GenericRepository repository : repositories) {
                        try {
                            repository.doStoreBuffer();
                        } catch (IOException e) {
                            LOGGER.error("Cannot batch store buffer", e);
                        }
                    }
                }
            }, 2, 2, TimeUnit.SECONDS);
        }

        void registerRepository(GenericRepository repository) {
            repositories.add(repository);
        }
    }
}


