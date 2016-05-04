package cn.larry.demo.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * Created by larryfu on 16-5-1.
 */
public class ConfigureWatcher implements Watcher {

    private ActiveKeyValueStore store ;

    public ConfigureWatcher(String host) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore();
        store.connect(host);
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
            try{
                displayConfig();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayConfig() throws KeeperException, InterruptedException {
        String value = store.read(ConfigureUpdater.PATH,this);
        System.out.printf("read %s as %s\n",ConfigureUpdater.PATH,value);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ConfigureWatcher watcher = new ConfigureWatcher("localhost");
        watcher.displayConfig();
        Thread.sleep(Long.MAX_VALUE);
    }
}
