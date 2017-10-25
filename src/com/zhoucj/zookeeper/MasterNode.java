package com.zhoucj.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;

/**
 * @author zhoucj
 */
public class MasterNode {

    Logger logger = Logger.getLogger(MasterNode.class);

    ZooKeeper zk;

    public void bootstrap() {
        createParent("/worker", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/status", new byte[0]);
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
                    logger.info("Parent created ");
                    break;

                case NODEEXISTS:
                    logger.info("Parent is already registered: " + path);
                    break;

                default:
                    logger.error("Something went wrong: ", KeeperException.create(KeeperException.Code.get(rc), path));
                    break;
            }
        }
    };
}
