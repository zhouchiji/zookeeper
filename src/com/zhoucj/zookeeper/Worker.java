package com.zhoucj.zookeeper;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


/**
 * @author zhoucj
 */
public class Worker implements Watcher {
    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
    ZooKeeper zk;
    String hostPort;
    Random random = new Random();
    String serviceId = Integer.toHexString(random.nextInt());

    public Worker(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZk() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println(event.toString() + "," + hostPort);

    }

    AsyncCallback.ChildrenCallback childrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            if (KeeperException.Code.get(rc) == KeeperException.Code.OK) {
                System.out.println("is OK");
            } else {
                isChanged();
            }
        }
    };

    public void isChanged() {
        zk.getChildren("/workers", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath() + " is activity");
                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    register();
                    isChanged();
                }
            }
        }, childrenCallback, null);
    }

    public void register() {
        zk.create("/workers/worker-" + serviceId, "Idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback, null);
    }

    AsyncCallback.StringCallback createWorkerCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;

                case OK:
                    System.out.println("Worker created ");
                    break;

                case NODEEXISTS:
                    System.err.println("Worker is already registered: " + serviceId);
                    break;

                default:
                    System.err.println("Something went wrong: " + KeeperException.create(KeeperException.Code.get(rc), path));
                    break;
            }
        }
    };

    public void shutdown() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws Exception {
        Worker w = new Worker("127.0.0.1:2181");
        w.startZk();
        w.register();
        Thread.sleep(10000);
        w.isChanged();
        Thread.sleep(60000);
        w.shutdown();
    }
}
