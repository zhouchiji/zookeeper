package com.zhoucj.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author zhoucj
 */
public class TaskDistribution {
    private String hostName;
    private ZooKeeper zk;

    public TaskDistribution(String hostName) {
        this.hostName = hostName;
    }

    public void start() throws IOException {
        zk = new ZooKeeper(hostName, 15000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
            }
        });
    }

    public static void main(String[] args) throws Exception {
        TaskDistribution td = new TaskDistribution("127.0.0.1:2181");
        td.start();
    }
}
