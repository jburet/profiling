package fr.xebia.log.server.store.hbase;

import fr.xebia.log.configuration.HbaseStoreConfiguration;
import fr.xebia.log.server.store.hbase.table.HBaseSchema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;

import java.io.IOException;


/**
 * Manage config and client of hbase store
 */
public class HbaseStore {

    private String serverList;

    private Configuration hbaseConfig;

    private HTable logTable;
    private HTable samplingTable;



    public HbaseStore(HbaseStoreConfiguration hbaseStoreConfiguration) {
        try {
            loadConfiguration(hbaseStoreConfiguration);
            initHbaseConfiguration();
            verifySchema();
            instanciateRepository();
        } catch (ZooKeeperConnectionException e) {
            throw new StorageException("Cannot init zookeeper cluster", e);
        } catch (MasterNotRunningException e) {
            throw new StorageException("Cannot init hbase", e);
        } catch (IOException e) {
            throw new StorageException("Connection error with hbase", e);
        }

    }

    private void initHbaseConfiguration() {
        hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.zookeeper.quorum", serverList);
    }

    private void instanciateRepository() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void verifySchema() throws IOException {
        HBaseAdmin admin = new HBaseAdmin(hbaseConfig);

        // Log table
        logTable = new HTable(hbaseConfig, HBaseSchema.LogTable.TABLE_NAME);
        HBaseSchema.LogTable.createSchemaIfNecessary(admin, logTable);

        // Sampling table
        samplingTable = new HTable(hbaseConfig, HBaseSchema.SamplingTable.TABLE_NAME);
        HBaseSchema.SamplingTable.createSchemaIfNecessary(admin, samplingTable);
    }

    private void loadConfiguration(HbaseStoreConfiguration hbaseStoreConfiguration) {
        this.serverList = hbaseStoreConfiguration.getServerList();
    }
}
