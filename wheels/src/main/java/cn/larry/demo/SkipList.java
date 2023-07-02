package cn.larry.demo;

import java.util.Random;

public class SkipList<T> {
    static class Node<T> {
        T data;
        Comparable<Object> key;
        private Node<T> next;
        private Node<T> pre;
        private Node<T> above;
        private Node<T> bottom;

    }

    private Node[] headers = new Node[16];

    private Node<T> head;

    private Node<T> tail;

    private int length;

    public Node findNode(Comparable<Object> key) {

        if (key == null || key.compareTo(head.key) < 0) {
            return head;
        }
        if (key.compareTo(head.key) == 0) {
            return head;
        }
        Node<T> node = head;
        while (node.next != null || node.bottom != null) {
            if (node.next == null) {
                node = node.bottom;
                continue;
            }
            if (key.compareTo(node.next.key) == 0) {
                return node.next;
            }
            if (key.compareTo(node.next.key) < 0) {
                if (node.bottom != null) {
                    node = node.bottom;
                } else {
                    return node.next;
                }
            } else {
                node = node.next;
            }
        }
        if (node.key.compareTo(key) <= 0) {
            return node;
        } else {
            return null;
        }
    }

    public void put(Comparable key, T data) {
        Node node = new Node();
        node.key = key;
        node.data = data;

        if (head == null) {
            head = node;
            tail = node;
            grow(node);
            return;
        }
        Node node1 = findNode(key);
        if (node1 == null) {
            node1.next = node;
            node.pre = node1;
            grow(node);
        } else if (node1.key.compareTo(key) == 0) {
            node1.data = data;
        } else {
            Node pre = node1.pre;
            if (pre != null) {
                pre.next = node;
                node.pre = pre;
            }
            node.next = node1;
            node1.pre = node;
            grow(node);
        }

    }

    //生长
    private void grow(Node n) {
        Random random = new Random();
        Node node = n;
        for (int i = 1; i < 16; i++) {
            if (!random.nextBoolean()) {
                return;
            }
            Node upper = new Node();
            upper.key = node.key;
            upper.data = node.data;
            upper.bottom = node;

            Node levelHead = headers[i];
            if (levelHead == null) {
                headers[i] = upper;
            }
            if (levelHead.key.compareTo(upper.key) > 0) {
                headers[i] = upper;
                upper.next = levelHead;
            } else {
                while (levelHead.next != null && levelHead.next.key.compareTo(upper.key) < 0) {
                    levelHead = levelHead.next;
                }
                upper.next = levelHead.next;
                levelHead.next = upper;
            }

        }

    }

}
