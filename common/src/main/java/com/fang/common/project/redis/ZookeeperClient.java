package com.fang.common.project.redis;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZookeeperClient {

    private static final int SESSION_TIME = 3000;

    private static ZooKeeper zookeeper = null;

    private static CountDownLatch latch;

    private static String zookeeperCluster;

    private static String appName;
    static {
        InputStream in = ZookeeperClient.class.getClassLoader().getResourceAsStream("zookeeper-config.properties");
        Properties properties = new Properties();
        try {
            if (in != null) {

                properties.load(in);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            zookeeperCluster = null;
        }
        zookeeperCluster = properties.getProperty("cluster");
        appName = properties.getProperty("application.name");

    }

    private ZookeeperClient() {
    }

    public static String getAppName() {
        return appName;
    }

    public static ZooKeeper getZooKeeper() {
        if (zookeeper == null) {
            synchronized (ZookeeperClient.class) {
                if (zookeeper == null) {
                    latch = new CountDownLatch(1);
                    zookeeper = buildClient();// 如果失败，下次还有成功的机会
                    try {
                        latch.await(30, TimeUnit.SECONDS);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    finally {
                        latch = null;
                    }
                }
            }
        }
        return zookeeper;
    }

    private static ZooKeeper buildClient() {
        try {
            return new ZooKeeper(zookeeperCluster, SESSION_TIME, new SessionWatcher());
        }
        catch (IOException e) {
            return null;
        }
    }

    public static void close() {
        if (zookeeper != null) {
            try {
                zookeeper.close();
                zookeeper = null;
            }
            catch (InterruptedException e) {
                // ignore exception
            }
        }
    }

    static class SessionWatcher implements Watcher {

        public void process(WatchedEvent event) {
            if (event.getState() == KeeperState.SyncConnected) {
                if (latch != null) {
                    latch.countDown();
                }
            } else if (event.getState() == KeeperState.Expired) {

                // session expired, may be never happending.
                // close old client and rebuild new client
                close();

                getZooKeeper();

            }
        }
    }

}
