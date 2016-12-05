package cn.larry.analyzer;

import java.util.HashMap;
import java.util.Map;

public class Trie {


    private Node root;

    private static class Node {
        private String val;
        private Map<Character, Node> next = new HashMap<>();
    }


    public void put(String key, String val) {
        root = put(root, key, 0, val);
    }

    private Node put(Node x, String key, int d, String val) {
        if (x == null) x = new Node();
        if (key.length() == d) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next.put(c, put(x.next.get(c), key, d + 1, val));
        return x;
    }


    public String longestPrefixOf(String s) {
        int len = search(root, s, 0, 0);
        return s.substring(0, len);
    }

    private int search(Node x, String s, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == s.length()) return length;
        char c = s.charAt(d);
        return search(x.next.get(c), s, d + 1, length);
    }

    public Interval longestPrefixWithRange(String s, int start, int end) {
        int len = search(root, s, 0, 0, start, end);
        if (len == 0) return null;
        return new Interval(start, start + len - 1);
    }

    private int search(Node x, String s, int d, int length, int start, int end) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == end - start) return length;
        char c = s.charAt(start + d);
        return search(x.next.get(c), s, d + 1, length, start, end);
    }

}
