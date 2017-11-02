package com.zhoucj.zookeeper;

import com.sun.jmx.snmp.tasks.ThreadService;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhouchiji
 */
public class SubNodeTask {

    private String host;
    private ZooKeeper zk;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Random random = new Random();
    private int serviceId = random.nextInt(TaskDistribution.workerList.size());

    public SubNodeTask(String host) {
        this.host = host;
    }

    public void getTasks() {
        zk.getChildren("/assign/workid-" + serviceId, newTaskWatcher, childrenCallback, dataCallback);
    }

    Watcher newTaskWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                assert ("/assign/workid-" + serviceId).equals(event.getPath());
                getTasks();
            }
        }
    };

    AsyncCallback.ChildrenCallback childrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            if (KeeperException.Code.get(rc) == KeeperException.Code.CONNECTIONLOSS) {
                getTasks();
            } else if (KeeperException.Code.get(rc) == KeeperException.Code.OK) {
                executor.execute(new Runnable() {
                    private List<String> children;
                    private DataCallback cb;
                    public Runnable init(List<String> children, DataCallback cb) {
                        this.children = children;
                        this.cb = cb;
                        return this;
                    }
                    @Override
                    public void run() {
                        synchronized (TaskDistribution.workerList) {
                            for (String task : children) {
                                if (!TaskDistribution.workerList.contains(task)) {
                                    zk.getData("/assign/workid-" + serviceId + "/" +task, false, cb, task);
                                    TaskDistribution.workerList.add(task);
                                }
                            }
                        }
                    }
                }.init(children, (DataCallback) ctx));
            }
        }
    };

    AsyncCallback.DataCallback dataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

        }
    };

    public void start() throws IOException {
        zk = new ZooKeeper(host, 15000, null);
    }

    public void stop() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws Exception {
        SubNodeTask st = new SubNodeTask("127.0.0.1:2181");
        st.start();
        Thread.sleep(5000);
        st.stop();
    }
}
