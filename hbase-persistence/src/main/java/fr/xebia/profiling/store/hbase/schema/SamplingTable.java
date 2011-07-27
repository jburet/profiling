package fr.xebia.profiling.store.hbase.schema;

public class SamplingTable {
    // Table name
    public static final String NAME = "sampling";

    // Column familly
    public static final String CF_APPLICATION = "application";
    public static final String CF_METHOD = "method";

    // Column
    public static final String CLUSTER_NAME = "cluster";
    public static final String SERVER_NAME = "server";
    public static final String APP_NAME = "app";
    public static final String AGENT_ID = "agent_id";
    public static final String THREAD_ID = "thread_id";
    public static final String CLASSNAME = "classname";
    public static final String METHOD = "method";
    public static final String TIMESTAMP = "timestamp";
    public static final String EXECUTION_TIME = "execution_time";

}
