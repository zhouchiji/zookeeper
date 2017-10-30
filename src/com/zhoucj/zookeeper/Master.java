package com.zhoucj.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Random;

/**
 * @author zhoucj
 */

public class Master implements Watcher {

    ZooKeeper zk;
    String host;
    Random r = new Random();
    static boolean isLeader = false;
    String serviceId = Integer.toHexString(r.nextInt());

    public Master(String host) {
        this.host = host;
    }

    /* public boolean checkMaster() {
         while (true) {
             try {
                 Stat stat = new Stat();
                 byte[] data = zk.getData("/master", false, stat);
                 isLeader = serviceId.getBytes().equals(data);
                 return isLeader;
             } catch (InterruptedException e) {
                 e.printStackTrace();
             } catch (KeeperException e) {
                 e.printStackTrace();
             }
         }
     }*/

    AsyncCallback.DataCallback checkCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    break;
                case NONODE:
                    runForMaster();
                    break;
                default:
                    break;
            }
        }
    };

    public void checkMaster() {
        zk.getData("/master", false, checkCallback, null);
    }

/*    public void runForMaster() {
        while (true) {
            try {
                zk.create("/master", serviceId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (InterruptedException e) {
                isLeader = false;
                break;
            } catch (KeeperException e) {
            }
        }
    }*/

    AsyncCallback.StringCallback createCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    break;
                case OK:
                    isLeader = true;
                    break;
                default:
                    isLeader = false;
                    break;
            }
            System.out.println("I'm " + (isLeader ? "" : "not ") + "the Leader");
        }
    };

    public void runForMaster() {
        zk.create("/master", serviceId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createCallback, null);
        try {
            byte[] b = zk.getData("/master", false, new Stat());
            for (byte b1 : b) {
                System.out.print(b1 + " ");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    AsyncCallback.StatCallback statCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            if (KeeperException.Code.get(rc) == KeeperException.Code.CONNECTIONLOSS) {
                isDeleted();
            } else if (KeeperException.Code.get(rc) == KeeperException.Code.OK) {
                if (stat == null) {
                    runForMaster();
                }
            }
        }
    };

    public void isDeleted(){
        zk.exists("/master", this, statCallback, null);
    }

    public void startUp() throws Exception {
        zk = new ZooKeeper(host, 15000, this);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDeleted) {
            runForMaster();
            isDeleted();
        }
    }

    public void stop() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws Exception {
        Master master = new Master("127.0.0.1:2181");
        master.startUp();
        master.runForMaster();
 /*       if (isLeader) {
            System.out.println("I'm the Leader");
            Thread.sleep(60000);
        } else {
            System.out.println("Someone else is Leader");
        }*/
        Thread.sleep(10000);
        master.isDeleted();
        Thread.sleep(60000);
        master.stop();
    }
}
