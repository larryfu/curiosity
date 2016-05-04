package cn.larry.demo.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by larryfu on 16-5-1.
 */
public class ConnectionWatcher implements Watcher{
    private static final int SESSION_TIMEOUT = 5000;
    protected ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);

    protected void connect(String host) throws IOException, InterruptedException {
        zk = new ZooKeeper(host, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
            connectedSignal.countDown();
    }

    public void close() throws InterruptedException {
        zk.close();
    }



}
