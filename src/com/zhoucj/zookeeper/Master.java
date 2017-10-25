package com.zhoucj.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Random;

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

    AsyncCallback.DataCallback dataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    break;
                case OK:
                    runForMaster();
                    break;
            }
        }
    };

    public void checkMaster() {
        zk.getData("/master", false, dataCallback, null);
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

    AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
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
        zk.create("/master", serviceId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, callback, null);
    }

    public void startUp() throws Exception {
        zk = new ZooKeeper(host, 15000, this);

    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public void stop() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws Exception {
        Master master = new Master("127.0.0.1:2181");
        master.startUp();
        master.checkMaster();
 /*       if (isLeader) {
            System.out.println("I'm the Leader");
            Thread.sleep(60000);
        } else {
            System.out.println("Someone else is Leader");
        }*/
//       Thread.sleep(60000);
        master.stop();
    }
}
