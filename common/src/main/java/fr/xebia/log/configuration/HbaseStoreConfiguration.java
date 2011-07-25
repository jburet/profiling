package fr.xebia.log.configuration;

/**
 * Interface for hbase configuration
 */
public interface HbaseStoreConfiguration {
    /**
     * Return server list for hbase.zookeeper.quorum
     * server1, server2, server n...
     * @return
     */
    String getServerList();

}
