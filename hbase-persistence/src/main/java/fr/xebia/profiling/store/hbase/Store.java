package fr.xebia.profiling.store.hbase;

import static fr.xebia.profiling.store.hbase.schema.SamplingTable.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Store {

    private final Configuration config;
    private HTable logTable;

    public Store() throws IOException {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "localhost");
        createSchemaIfNecessary();
    }

    private void createSchemaIfNecessary() throws IOException {
        HBaseAdmin admin = new HBaseAdmin(config);
        logTable = new HTable(config, NAME);
        if (!admin.tableExists(NAME)) {
            admin.createTable(new HTableDescriptor(NAME));
            admin.disableTable(NAME);
            HColumnDescriptor cf1 = new HColumnDescriptor(CF_APPLICATION);
            HColumnDescriptor cf2 = new HColumnDescriptor(CF_METHOD);
            admin.addColumn(NAME, cf1);
            admin.addColumn(NAME, cf2);
            admin.enableTable(NAME);
        }
    }

    public void storeMethodInterceptor(MethodInterceptor mi) throws IOException {

        Put put = new Put(Bytes.toBytes(mi.getUniqueKey()));
        put.add(CF_LOG, CLASS_NAME, Bytes.toBytes(mi.getClassName()));
        put.add(CF_LOG, METHOD_NAME, Bytes.toBytes(mi.getMethodName()));
        put.add(CF_LOG, EXECUTION_TIME, Bytes.toBytes(mi.getExecutionTime()));
        for (int i = 0; i < mi.getParamType().length; i++) {
            put.add(CF_LOG, LogTable.getParamType(i), Bytes.toBytes(mi.getParamType()[i]));
        }
        for (int i = 0; i < mi.getParamValue().length; i++) {
            put.add(CF_LOG, LogTable.getParamValue(i), Bytes.toBytes(mi.getParamValue()[i]));
        }
        put.add(CF_LOG, RETURN_TYPE, Bytes.toBytes(mi.getReturnType()));
        put.add(CF_LOG, RETURN_VALUE, Bytes.toBytes(mi.getReturnValue()));

        logTable.put(put);
    }

    public void storeMethodInterceptorBatch(MethodInterceptor[] mi) throws IOException {
        List<Put> puts = new ArrayList<Put>(mi.length);
        for (int i = 0; i < mi.length; i++) {
            puts.add(convertMethodInterceptorToPut(mi[i]));
        }
        logTable.put(puts);
    }

    private Put convertMethodInterceptorToPut(MethodInterceptor mi) {
        Put put = new Put(Bytes.toBytes(mi.getUniqueKey()));
        put.add(CF_LOG, CLASS_NAME, Bytes.toBytes(mi.getClassName()));
        put.add(CF_LOG, METHOD_NAME, Bytes.toBytes(mi.getMethodName()));
        put.add(CF_LOG, EXECUTION_TIME, Bytes.toBytes(mi.getExecutionTime()));
        for (int i = 0; i < mi.getParamType().length; i++) {
            put.add(CF_LOG, LogTable.getParamType(i), Bytes.toBytes(mi.getParamType()[i]));
        }
        for (int i = 0; i < mi.getParamValue().length; i++) {
            put.add(CF_LOG, LogTable.getParamValue(i), Bytes.toBytes(mi.getParamValue()[i]));
        }
        put.add(CF_LOG, RETURN_TYPE, Bytes.toBytes(mi.getReturnType()));
        put.add(CF_LOG, RETURN_VALUE, Bytes.toBytes(mi.getReturnValue()));
        return put;
    }

}
