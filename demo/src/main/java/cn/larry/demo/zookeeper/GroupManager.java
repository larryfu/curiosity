package cn.larry.demo.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * Created by larryfu on 16-5-1.
 */
public class GroupManager extends ConnectionWatcher {

    public void exists(String groupName){
        String path = "/"+groupName;
        zk.exists(path,this,null,null);
    }


    public void create(String groupName) throws KeeperException, InterruptedException {
        String path = "/" + groupName;
        String createdPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("created " + createdPath);
    }

    public void join(String groupName, String memberName) throws KeeperException, InterruptedException {
        String path = "/" + groupName + "/" + memberName;
        String createdPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("joined " + createdPath);
    }

    public void list(String groupName) {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            System.out.println("children:");
            children.forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void delete(String groupName) {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            for (String child : children)
                zk.delete(path + "/" + child, -1);
            zk.delete(path,-1);
            children.forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void write(String path, String value) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, false);
        if (stat == null) {
            zk.create(path, value.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            zk.setData(path, value.getBytes(StandardCharsets.UTF_8), -1);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        GroupManager group = new GroupManager();
        group.connect("localhost");
        group.exists("zoo");
        group.create("zoo");
//        group.create("zoo");
//        group.join("zoo", "lion");
//        group.join("zoo", "tiger");
//        group.join("zoo", "panda");
//        group.list("zoo");
//        group.delete("zoo");
//      //  group.close();
//        group.list("zoo");
    }

}
