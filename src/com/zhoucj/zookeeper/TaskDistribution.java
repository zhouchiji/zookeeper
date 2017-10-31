package com.zhoucj.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author zhoucj
 */
public class TaskDistribution {
    private String hostName;
    private ZooKeeper zk;
    private List<String> workerList;
    private Random rand = new Random();

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

    public void getChildren() {
        zk.getChildren("/tasks", childrenWatcher, childrenCallback, null);
    }

    private Watcher childrenWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                assert "/tasks".equals(event.getPath());
                getChildren();
            }
        }
    };

    private AsyncCallback.ChildrenCallback childrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            if (KeeperException.Code.get(rc) == KeeperException.Code.OK) {
                if (children != null) {
                    removeAndSet(children);
                    getChild(children);
                }
            } else if (KeeperException.Code.get(rc) == KeeperException.Code.CONNECTIONLOSS) {
                getChildren();
            } else {
                System.err.println(KeeperException.Code.get(rc) + "error");
            }
        }
    };

    private void removeAndSet(List<String> children) {
        if (workerList == null || workerList.isEmpty()) {
            workerList = children;
        } else {
            for (String s : workerList) {
                if (!children.contains(s)) {
                    workerList.remove(s);
                }
            }
            for (String child : children) {
                if (!workerList.contains(child)) {
                    workerList.add(child);
                }
            }
        }
    }

    public void getChild(List<String> children) {
        for (String child : children) {
            getChildData(child);
        }
    }

    public void getChildData(String child) {
        zk.getData("/tasks/" + child, false, taskDataCallback, child);
    }

    private AsyncCallback.DataCallback taskDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            if (KeeperException.Code.get(rc) == KeeperException.Code.CONNECTIONLOSS) {
                getChildData((String) ctx);
            } else if (KeeperException.Code.get(rc) == KeeperException.Code.OK) {
                int index = rand.nextInt(workerList.size());
                String assignPath = "/assign/" + workerList.get(index) + "/" + ctx;
                createAssign(assignPath, data);
            }
        }
    };

    public void createAssign(String path, byte[] data) {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createCallback, data);
    }

    AsyncCallback.StringCallback createCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            if (KeeperException.Code.get(rc) == KeeperException.Code.OK) {
                deleteTask(name.substring(name.lastIndexOf("/")+1));
            }
        }
    };

    public void deleteTask(String path){
        zk.delete("/tasks/"+path, -1, deleteCallback, null);
    }

    AsyncCallback.VoidCallback deleteCallback = new AsyncCallback.VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx) {
        }
    };

    public static void main(String[] args) throws Exception {
        TaskDistribution td = new TaskDistribution("127.0.0.1:2181");
        td.start();
    }
}
