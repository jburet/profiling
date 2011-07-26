package fr.xebia.log.server.store.hbase.repository;


import fr.xebia.log.server.store.hbase.table.HBaseSchema;
import fr.xebia.log.transport.thrift.MethodExecution;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestLogRepository {
    private HBaseTestingUtility testUtil = new HBaseTestingUtility();

    private HTable logTable;
    private LogRepository logRepository;

    public TestLogRepository() throws Exception {
        testUtil.startMiniCluster(1);
        this.logTable = testUtil.createTable(Bytes.toBytes(HBaseSchema.LogTable.TABLE_NAME),
                new byte[][]{Bytes.toBytes(HBaseSchema.LogTable.CF_SERVER), Bytes.toBytes(HBaseSchema.LogTable.CF_SERVER)});
        this.logRepository = new LogRepository(logTable);
    }

    @Test
    public void when_insert_log_synchonous_then_I_can_query_it_by_id() throws IOException {
        Map<String, String> param = new HashMap<String, String>();
        param.put("java.lang.String", "test-param");
        MethodExecution me = new MethodExecution("cluster-id",
                "server-id",
                "agent-id",
                "thread-1",
                "1",
                "fr.xebia.log.test.Test",
                "unitTest",
                param,
                1200000);
        logRepository.synchronousStore(me);
        // get the method execution by id
    }
}
