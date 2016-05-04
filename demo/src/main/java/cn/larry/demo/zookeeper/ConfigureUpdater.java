package cn.larry.demo.zookeeper;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by larryfu on 16-5-1.
 */
public class ConfigureUpdater {
    public static final String PATH = "/config";
    private ActiveKeyValueStore store;
    private Random random = new Random();

    public ConfigureUpdater(String host) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore();
        store.connect(host);
    }

    public void run() throws InterruptedException, KeeperException {
        while (true) {
            String value = random.nextInt(100) + "";
            store.write(PATH, value);
            System.out.printf("set %s to %s \n", PATH, value);
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        }
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        ConfigureUpdater updater = new ConfigureUpdater("localhost");
        updater.run();
    }
}
