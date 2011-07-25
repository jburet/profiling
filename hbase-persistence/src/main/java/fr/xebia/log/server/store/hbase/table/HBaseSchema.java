package fr.xebia.log.server.store.hbase.table;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseSchema {

    private static class GenericTable {
        /**
         * Column family for server identification
         */
        public static final String CF_SERVER = "server";

        public static final String C_APP_NAME = "app-name";
        public static final String C_CLUSTER_NAME = "cluster-name";
        public static final String C_SERVER_NAME = "server-name";
        public static final String C_AGENT_IDENTIFIER = "agent-identifier";
    }

    /**
     * Metadata for store of logging
     */
    public static class LogTable extends GenericTable {

        /**
         * Table name
         */
        public static final String TABLE_NAME = "log";

        /**
         * Log
         */
        public static final String CF_LOG = "log";
        public static final String C_CLASS_NAME = "class-name";
        public static final String C_METHOD_NAME = "method-name";
        public static final String C_ARG_TYPE_PREFIX = "arg-type-";
        public static final String C_ARG_VALUE_PREFIX = "arg-value-";

        public static void createSchemaIfNecessary(HBaseAdmin admin, HTable logTable) throws IOException {
            // Create table if not exist
            if (!admin.tableExists(LogTable.TABLE_NAME)) {
                admin.createTable(new HTableDescriptor(LogTable.TABLE_NAME));
            }
            admin.disableTable(LogTable.TABLE_NAME);

            // Create column family CF_SERVER if needed
            if (logTable.getTableDescriptor().getFamily(Bytes.toBytes(LogTable.CF_SERVER)) == null) {
                HColumnDescriptor cf1 = new HColumnDescriptor(LogTable.CF_SERVER);
                admin.addColumn(LogTable.TABLE_NAME, cf1);
            }

            // Create column family CF_LOG if needed
            if (logTable.getTableDescriptor().getFamily(Bytes.toBytes(LogTable.CF_LOG)) == null) {
                HColumnDescriptor cf2 = new HColumnDescriptor(LogTable.CF_LOG);
                admin.addColumn(LogTable.TABLE_NAME, cf2);
            }
            admin.enableTable(LogTable.TABLE_NAME);
        }
    }


    /**
     * Metadata for store of sampling
     */
    public static class SamplingTable extends GenericTable {
        /**
         * Table name
         */
        public static final String TABLE_NAME = "sampling";

        /**
         * Sampling
         */
        public static final String CF_SAMPLING = "sampling";
        public static final String C_CLASS_NAME = "class-name";
        public static final String C_METHOD_NAME = "method-name";
        public static final String C_EXECUTION = "execution";
        public static final String C_EXECUTION_TIMES = "execution-time";

        public static void createSchemaIfNecessary(HBaseAdmin admin, HTable samplingTable) throws IOException {
            // Create table if not exist
            if (!admin.tableExists(SamplingTable.TABLE_NAME)) {
                admin.createTable(new HTableDescriptor(SamplingTable.TABLE_NAME));
            }
            admin.disableTable(SamplingTable.TABLE_NAME);

            // Create column family CF_SERVER if needed
            if (samplingTable.getTableDescriptor().getFamily(Bytes.toBytes(SamplingTable.CF_SERVER)) == null) {
                HColumnDescriptor cf1 = new HColumnDescriptor(SamplingTable.CF_SERVER);
                admin.addColumn(SamplingTable.TABLE_NAME, cf1);
            }

            // Create column family CF_SAMPLING if needed
            if (samplingTable.getTableDescriptor().getFamily(Bytes.toBytes(SamplingTable.CF_SAMPLING)) == null) {
                HColumnDescriptor cf2 = new HColumnDescriptor(SamplingTable.CF_SAMPLING);
                admin.addColumn(SamplingTable.TABLE_NAME, cf2);
            }
            admin.enableTable(SamplingTable.TABLE_NAME);
        }
    }
}