package fr.xebia.log.server.store.hbase.repository;

import fr.xebia.log.buffer.CircularBuffer;
import fr.xebia.log.transport.thrift.MethodExecution;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;

/**
 * Repository for Log object
 */
public class LogRepository extends GenericRepository<MethodExecution> {

    public HTable logTable;

    public LogRepository(HTable logTable) {
        this.logTable = logTable;
    }

    @Override
    Put convertObjectToPut(MethodExecution object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    HTable getTable() {
        return logTable;
    }
}
