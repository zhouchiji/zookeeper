package com.zhoucj.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author zhoucj
 */
public class MasterNode implements Watcher {

    ZooKeeper zk;

    public void bootstrap() {
        createParent("/worker", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/status", new byte[0]);
    }

    public MasterNode() throws IOException {
        zk = new ZooKeeper("127.0.0.1:2181", 20000, this);
    }

    public void createParent(String path, byte[] data) {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createParentCallback, data);
    }

    AsyncCallback.StringCallback createParentCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createParent(path, (byte[]) ctx);
                    break;

                case OK:
                    System.out.println("Parent created ");
                    break;

                case NODEEXISTS:
                    System.out.println("Parent is already registered: " + path);
                    break;

                default:
                    System.err.println("Something went wrong: "+ KeeperException.create(KeeperException.Code.get(rc), path));
                    break;
            }
        }
    };

    public void stop() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MasterNode mn = new MasterNode();
        mn.bootstrap();
        Thread.sleep(60000);
        mn.stop();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }
}
