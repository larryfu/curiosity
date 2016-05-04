package cn.larry.demo.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by larryfu on 16-5-1.
 */
public class ActiveKeyValueStore extends ConnectionWatcher {
    public void write(String path, String value) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, false);
        if (stat == null) {
            zk.create(path, value.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            zk.setData(path, value.getBytes(StandardCharsets.UTF_8), -1);
        }
    }

    public String read(String path, Watcher watcher) throws KeeperException, InterruptedException {
        byte[] data = zk.getData(path, watcher, null);
        return new String(data, StandardCharsets.UTF_8);
    }

}
