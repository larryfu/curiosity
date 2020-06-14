package cn.larry.leetcode;

public class LinkedList<K> {

     Node<K> head;
     int size;
     Node<K> tail;

    public LinkedList() {
        size = 0;
    }

    public Node<K> add(K k) {
        Node node = new Node<>(k);
        if (head == null) {
            head = node;
            tail = head;
        } else {
            tail.next = node;
            node.pre = tail;
            tail = node;
        }
        size++;
        return node;
    }

    public Node<K> addHead(K k) {
        Node node = new Node<>(k);
        if (head == null) {
            head = node;
            tail = head;
        } else {
            node.next = head;
            head.pre = node;
            head = node;
        }
        size++;
        return node;
    }

    public void removeTail(){
        if(tail!=null){
            tail = tail.pre;
            if(tail!=null){
                tail.next = null;
            }
        }
    }

    public K get(int n) {
        if (n >= size) return null;
        Node node = head;
        for (int i = 0; i < n; i++) {
            node = node.next;
        }
        return (K) node.t;
    }

    public boolean removeIndex(int n) {
        if (n >= size) return false;
        Node node = head;
        for (int i = 0; i < n; i++) {
            node = node.next;
        }
        if (node.pre != null) {
            node.pre.next = node.next;
        }
        if (node.next != null) {
            node.next.pre = node.pre;
        }
        if (node == head) head = node.next;
        if (node == tail) tail = node.pre;
        size--;
        return true;
    }

    public static class Node<T> {
        T t;
        Node<T> pre;
        Node<T> next;

        public Node(T t) {
            this.t = t;
        }
    }
}


