package cn.larry.leetcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LRUCache {

    private int size;

    public LRUCache(int size) {
        cacheMap = new HashMap<>();
        list = new LinkedList<>();
        this.size = size;
    }


    Map<String, LinkedList.Node<String>> cacheMap;

    private LinkedList<String> list;

    public String get(String key) {
       // Arrays.so
        LinkedList.Node<String> node = cacheMap.get(key);
        if (node.pre != null) {
            node.pre.next = node.next;
        }
        if (node.next != null) {
            node.next.pre = node.pre;
        }
        node.next = list.head;
        node.pre = null;
        list.head = node;
        return node.t;
    }

    public void put(String key, String value) {
        LinkedList.Node<String> node = list.addHead(value);
        cacheMap.put(key, node);
        while (list.size>size){
            list.removeTail();
            //TODO 在map中移除
        }
    }
}
